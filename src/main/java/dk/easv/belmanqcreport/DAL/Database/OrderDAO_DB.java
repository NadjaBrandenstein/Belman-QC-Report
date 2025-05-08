package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.Interface.IOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class OrderDAO_DB implements IOrder {

    @Override
    public List<Order> getAllOrder() throws Exception {
        DBConnection dbConnection = new DBConnection();


        String sql = "SELECT o.orderID, o.userID, o.orderNumber, " +
                "t.orderItem, i.imageID, i.imagePath, i.comment " +
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
    public Order createOrder(Order order) throws Exception {
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
    public Order updateOrder(Order order) throws Exception {
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
    public void deleteOrder(Order order) throws Exception {
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
}
