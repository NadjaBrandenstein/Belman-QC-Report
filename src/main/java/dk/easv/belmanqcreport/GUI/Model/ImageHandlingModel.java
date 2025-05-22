package dk.easv.belmanqcreport.GUI.Model;

import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.BLL.Manager.OrderManager;

import java.util.List;
import java.util.Optional;

public class ImageHandlingModel {

    private OrderManager orderManager;


    public ImageHandlingModel() {
        orderManager = new OrderManager();

    }

    public List<Order> getAllOrders() throws Exception {
        return orderManager.getAllOrders();
    }

    public List<OrderItem> getItemsByOrderID(int orderID) throws Exception {
        return orderManager.getItemsByOrderID(orderID);
    }

    public void updateOrder(Order order) throws Exception {
        orderManager.updateOrders(order);
    }

    public Optional<Order> findOrderByNumber(String orderNumber) throws Exception {
        return orderManager.getOrderByNumber(orderNumber);
    }

    public List<OrderItem> getItemsByOrderNumber(String orderNumber) throws Exception {
        return orderManager.getItemsByOrderNumber(orderNumber);
    }



}
