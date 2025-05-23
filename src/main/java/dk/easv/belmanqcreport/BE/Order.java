package dk.easv.belmanqcreport.BE;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderID;
    private int userID;
    private String orderNumber;
    private String orderItem;
    private String log;

    private List<MyImage> images = new ArrayList<>();

    public Order(int orderID, int userID, String orderNumber, String orderItem, String log) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderNumber = orderNumber;
        this.orderItem = orderItem;
        this.log = log;
    }

    public Order(int orderID, int userID, String orderNumber, String orderItem) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderNumber = orderNumber;
        this.orderItem = orderItem;
    }

    public Order() {

    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public List<MyImage> getImages() {
        return images;
    }

    public void setImages(List<MyImage> images) {
        this.images = images;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public void add(Order order) {
        setOrderID(order.getOrderID());

    }

    @Override
    public String toString() {
        return orderNumber;
    }

    public int getOrderItemID() {
        return 0;
    }

    public String getItemNumber() {
        return orderNumber.substring(orderNumber.lastIndexOf("-") + 1);
    }
}