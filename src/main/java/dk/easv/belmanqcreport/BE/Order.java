package dk.easv.belmanqcreport.BE;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderID;
    private int userID;
    private String orderNumber;

    private List<MyImage> images = new ArrayList<>();

    public Order(int orderID, int userID, String orderNumber) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderNumber = orderNumber;

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

    public void setOrderNumber(String orderNumber) {this.orderNumber = orderNumber;}

    public String getOrderNumber() {return orderNumber;}


    public void add(Order order) {
        setOrderID(order.getOrderID());

    }

}