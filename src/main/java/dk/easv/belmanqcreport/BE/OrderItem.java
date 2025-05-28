package dk.easv.belmanqcreport.BE;
// Java Imports
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

    public List<MyImage> getImages() {
        return images;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public String getOrderItem() {
        return orderItem;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (!(o instanceof OrderItem )) return false;
        return this.orderItemId == ((OrderItem) o).orderItemId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(orderItemId);
    }

    @Override
    public String toString() {
        return orderItem;
    }
}
