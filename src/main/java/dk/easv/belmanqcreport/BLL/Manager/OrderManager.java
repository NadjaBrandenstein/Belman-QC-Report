package dk.easv.belmanqcreport.BLL.Manager;

import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.DAL.Database.OrderDAO_DB;

import java.util.List;
import java.util.Optional;

public class OrderManager {

    private OrderDAO_DB orderDAO;

    public OrderManager() {

        orderDAO = new OrderDAO_DB();

    }

    public List<Order> getAllOrders() throws Exception {
        return orderDAO.getAllOrder();
    }
    public Order createOrders(Order order) throws Exception {
        return orderDAO.createOrder(order);
    }
    public Order updateOrders(Order order) throws Exception {
        return orderDAO.updateOrder(order);
    }
    public void deleteOrders(Order order) throws Exception {
        orderDAO.deleteOrder(order);
    }
    public List<OrderItem> getItemsByOrderID(int orderID) throws Exception {
       return orderDAO.getItemsByOrderID(orderID);
    }
    public Optional<Order> getOrderByNumber(String orderNumber) throws Exception {
        return orderDAO.getOrderByNumber(orderNumber);
    }
    public List<OrderItem> getItemsByOrderNumber(String orderNumber) throws Exception {
        return orderDAO.getItemsByOrderNumber(orderNumber);
    }

}
