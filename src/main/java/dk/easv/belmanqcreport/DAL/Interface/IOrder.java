package dk.easv.belmanqcreport.DAL.Interface;

import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;

import java.util.List;
import java.util.Optional;

public interface IOrder {
    List<Order> getAllOrder() throws Exception;
    Order createOrder(Order order) throws Exception;
    Order updateOrder(Order order) throws Exception;
    void deleteOrder(Order order) throws Exception;

    List<OrderItem> getItemsByOrderID(int orderID) throws Exception;

    Optional<Order> getOrderByNumber(String orderNumber) throws Exception;

    List<OrderItem> getItemsByOrderNumber(String orderNumber) throws Exception;
}
