package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Orderv2;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.Interface.IOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class OrderDAO_DB implements IOrder {

    @Override
    public List<Orderv2> getAllOrder() throws Exception {
        DBConnection dbConnection = new DBConnection();


        String sql = "SELECT o.orderID, o.userID, o.orderNumber, " +
                "t.orderItem, i.imageID, i.imagePath, i.comment " +
                "FROM dbo.[Order] AS o " +
                "LEFT JOIN dbo.Item AS t ON o.orderID = t.orderID " +
                "LEFT JOIN dbo.Image AS i ON t.orderItemID = i.orderItemID " +
                "ORDER BY o.orderID";


        Map<Integer, Orderv2> orderMap = new HashMap<>();

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int orderID = rs.getInt("orderID");
                int userID = rs.getInt("userID");
                String orderNumber = rs.getString("orderNumber");
                String orderItem = rs.getString("orderItem");

                Orderv2 order = orderMap.computeIfAbsent(orderID,
                        id -> new Orderv2(id, userID, orderNumber, orderItem));


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
    public Orderv2 createOrder(Orderv2 order) throws Exception {
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
    public Orderv2 updateOrder(Orderv2 order) throws Exception {
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
    public void deleteOrder(Orderv2 order) throws Exception {
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
