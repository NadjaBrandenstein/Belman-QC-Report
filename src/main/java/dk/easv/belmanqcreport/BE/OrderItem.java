package dk.easv.belmanqcreport.BE;

import java.util.ArrayList;
import java.util.List;

public class OrderItem {

    private int orderItemId;
    private int orderId;
    private String orderItem;

    private List<MyImage> images = new ArrayList<>();

    public OrderItem(int orderItemId, int orderId, String orderItem) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.orderItem = orderItem;
    }

    public OrderItem(int orderItemId, String orderItem) {
        this.orderItemId = orderItemId;
        this.orderItem = orderItem;
    }

    public OrderItem() {

    }

    public List<MyImage> getImages() {
        return images;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }



    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }



    public void setOrderItem(String orderIte) {
        this.orderItem = orderIte;
    }

    @Override
    public String toString() {
        return orderItem;
    }
}
