package dk.easv.belmanqcreport.GUI.Model;

import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.BLL.Manager.OrderManager;
import javafx.fxml.FXML;

import java.util.List;

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



}
