package dk.easv.belmanqcreport.BLL.UTIL;

import dk.easv.belmanqcreport.BE.Order;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetOrderNumberFromDatabase {
    private final DataSource dataSource;

    public GetOrderNumberFromDatabase(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Order getOrder(int orderId) {
        Order order = new Order();
        try (Connection conn = dataSource.getConnection()){
            String sql = "SELECT orderNumber FROM orders WHERE orderID = ?";
            try (PreparedStatement stmt = conn.prepareStatement("SELECT orderNumber FROM orders WHERE orderID = ?")) {
                stmt.setInt(1, orderId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String orderNumber = rs.getString("orderNumber");
                            order.setOrderNumber(rs.getString("orderNumber"));
                }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fejl ved hentning af ordre: " + e.getMessage(), e);
        }
        return order;
    }
}
