package dk.easv.belmanqcreport.DAL.Interface;

import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;

import java.util.List;

public interface IOrder {
    List<Order> getAllOrder() throws Exception;
    Order createOrder(Order order) throws Exception;
    Order updateOrder(Order order) throws Exception;
    void deleteOrder(Order order) throws Exception;

    List<OrderItem> getItemsByOrderID(int orderID) throws Exception;
}
