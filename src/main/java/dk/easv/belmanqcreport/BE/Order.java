package dk.easv.belmanqcreport.BE;
// Java Imports
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderID;
    private int userID;
    private String orderNumber;
    private String orderItem;

    private List<MyImage> images = new ArrayList<>();

    public Order(int orderID, int userID, String orderNumber, String orderItem) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderNumber = orderNumber;
        this.orderItem = orderItem;
    }

    public Order() {

    }

    public List<MyImage> getImages() {
        return images;
    }

    public int getOrderID() {
        return orderID;
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

    @Override
    public String toString() {
        return orderNumber;
    }

}