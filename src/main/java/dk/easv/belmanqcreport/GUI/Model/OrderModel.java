package dk.easv.belmanqcreport.GUI.Model;
// Project Imports
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BLL.Manager.OrderManager;
// Java Imports
import java.util.List;

public class OrderModel {

    private OrderManager orderManager;

    public OrderModel() {
        orderManager = new OrderManager();
    }

    public List<Order> getAllOrders() throws Exception {
        return orderManager.getAllOrders();
    }

    public boolean doesOrderExist(String orderNumber) throws Exception {
        return getAllOrders().stream()
                .anyMatch(order -> order.getOrderNumber().equalsIgnoreCase(orderNumber));
    }
}
