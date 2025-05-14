package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.Interface.IRepository;

import java.sql.*;
import java.util.*;

public class OrderRepository implements IRepository<Order> {

    @Override
    public List<Order> getAll() throws Exception {
        DBConnection dbConnection = new DBConnection();


        String sql = "SELECT o.orderID, o.userID, o.orderNumber, " +
                "t.orderItem, t.orderItemID, i.imageID, i.imagePath, i.comment " +
                "FROM dbo.[Order] AS o " +
                "LEFT JOIN dbo.Item AS t ON o.orderID = t.orderID " +
                "LEFT JOIN dbo.Image AS i ON t.orderItemID = i.orderItemID " +
                "ORDER BY o.orderID";


        Map<Integer,Order> orderMap = new HashMap<>();

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int orderID = rs.getInt("orderID");
                int userID = rs.getInt("userID");
                String orderNumber = rs.getString("orderNumber");
                String orderItem = rs.getString("orderItem");

                Order order = orderMap.computeIfAbsent(orderID,
                        id -> new Order(id, userID, orderNumber, orderItem));


                if (order.getOrderItem() == null && orderItem != null) {
                    order.setOrderItem(orderItem);
                }

                int imageID = rs.getInt("imageID");
                String imagePath = rs.getString("imagePath");
                String comment = rs.getString("comment");

                if (imagePath != null) {
                    MyImage image = new MyImage(imageID, imagePath, comment);
                    order.getImages().add(image);
                }
            }
        }
        return new ArrayList<>(orderMap.values());
    }


    @Override
    public Order add(Order order) throws Exception {
        DBConnection dbConnection = new DBConnection();
        String sql = "INSERT INTO Order (orderID) VALUES (?,?,?)";


        try (Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getOrderID());


            stmt.executeUpdate();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public Order update(Order order) throws Exception {
        DBConnection dbConnection = new DBConnection();
        String sql = "UPDATE [Order] SET orderID = ?";

        try (Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setInt(1, order.getOrderID());

            stmt.executeUpdate();
        }

        return order;
    }

    @Override
    public void delete(Order order) throws Exception {
        DBConnection dbConnection = new DBConnection();
        String sql = "DELETE FROM Order WHERE orderID = ?";

        try (Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getOrderID());

            stmt.executeUpdate();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<OrderItem> getItemsByOrderID(int orderID) throws Exception {
        DBConnection dbConnection = new DBConnection();
        String sql =
                "SELECT orderItemID, orderID, orderItem " +
                        "  FROM dbo.Item " +
                        " WHERE orderID = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderID);

            try (ResultSet rs = stmt.executeQuery()) {
                List<OrderItem> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(new OrderItem(
                            rs.getInt("orderItemID"),
                            rs.getInt("orderID"),
                            rs.getString("orderItem")
                    ));
                }
                return items;
            }
        }
    }

    @Override
    public Optional<Order> getOrderByNumber(String orderNumber) throws Exception {
        String sql = "SELECT orderID, userID, orderNumber " +
                "FROM dbo.[Order] " +
                "WHERE orderNumber = ?";
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, orderNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if(!rs.next()) return Optional.empty();
                int orderID = rs.getInt("orderID");
                int userID = rs.getInt("userID");
                return Optional.of(new Order(orderID, userID, orderNumber, null));
            }
        }


    }

    @Override
    public List<OrderItem> getItemsByOrderNumber(String orderNumber) throws Exception {
        String sql = "SELECT item.orderItemID, item.orderID, item.orderItem " +
                "FROM dbo.Item AS item " +
                "JOIN dbo.[Order] AS ord ON ord.orderID = item.orderID " +
                "WHERE ord.orderNumber = ?";
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, orderNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                List<OrderItem> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(new OrderItem(
                            rs.getInt("orderItemID"),
                            rs.getInt("orderID"),
                            rs.getString("orderItem")
                    ));
                }
                return items;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
