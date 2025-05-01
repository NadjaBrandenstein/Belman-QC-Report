package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.Interface.IOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDAO_DB implements IOrder {

    @Override
    public List<Order> getAllOrder() throws Exception {
        DBConnection dbConnection = new DBConnection();
        List<Order> orders = new ArrayList<>();

        String sql = "SELECT orderID, userID, imagePath, comment FROM [Order]";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int orderID = rs.getInt("orderID");
                int userID = rs.getInt("userID");
                String imagePath = rs.getString("imagePath");
                String comment = rs.getString("comment");


                Order order = new Order(orderID, userID, imagePath.toString(), comment);
                orders.add(order);
            }
        }
        return orders;
    }


    @Override
    public Order createOrder(Order order) throws Exception {
        DBConnection dbConnection = new DBConnection();
        String sql = "INSERT INTO Order (orderID, imagePath, comment) VALUES (?,?,?)";


        try (Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getOrderID());
            stmt.setString(2, order.getImagePath());
            stmt.setString(3, order.getComment());

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
        String sql = "UPDATE [Order] SET imagePath = ?, comment = ? WHERE orderID = ?";

        try (Connection conn = dbConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getImagePath());
            stmt.setString(2, order.getComment());
            stmt.setInt(3, order.getOrderID());

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
