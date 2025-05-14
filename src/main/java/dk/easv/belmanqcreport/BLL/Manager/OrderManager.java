package dk.easv.belmanqcreport.BLL.Manager;

import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.DAL.Database.OrderRepository;

import java.util.List;
import java.util.Optional;

public class OrderManager {

    private OrderRepository orderRepository;

    public OrderManager() {

        orderRepository = new OrderRepository();

    }

    public List<Order> getAllOrders() throws Exception {
        return orderRepository.getAll();
    }
    public Order createOrders(Order order) throws Exception {
        return orderRepository.add(order);
    }
    public Order updateOrders(Order order) throws Exception {
        return orderRepository.update(order);
    }
    public void deleteOrders(Order order) throws Exception {
        orderRepository.delete(order);
    }
    public List<OrderItem> getItemsByOrderID(int orderID) throws Exception {
       return orderRepository.getItemsByOrderID(orderID);
    }
    public Optional<Order> getOrderByNumber(String orderNumber) throws Exception {
        return orderRepository.getOrderByNumber(orderNumber);
    }
    public List<OrderItem> getItemsByOrderNumber(String orderNumber) throws Exception {
        return orderRepository.getItemsByOrderID(Integer.parseInt(orderNumber));
    }

}
