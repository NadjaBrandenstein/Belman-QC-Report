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


        String sql = "SELECT o.orderID, o.userID, i.imageID, i.imagePath, i.comment from dbo.[Order] as o " +
                    "LEFT JOIN dbo.Image as i on o.orderID = i.orderID ORDER BY o.orderID";

        Map<Integer,Order> orderMap = new HashMap<>();

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int orderID = rs.getInt("orderID");
                int userID = rs.getInt("userID");

                Order order = orderMap.computeIfAbsent(orderID,
                        id -> new Order(id, userID));

                int imageID = rs.getInt("imageID");
                String imagePath = rs.getString("imagePath");
                String comment = rs.getString("comment");

                if(imagePath != null) {
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
