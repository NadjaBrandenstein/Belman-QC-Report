package dk.easv.belmanqcreport.BE;

public class OrderItem {
    private int orderId;
    private int orderItemId;
    private String orderItem;

    public OrderItem(int orderId, int orderItemId, String orderIte) {
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.orderItem = orderIte;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getOrderIte() {
        return orderItem;
    }

    public void setOrderIte(String orderIte) {
        this.orderItem = orderIte;
    }

    @Override
    public String toString() {
        return orderItem;
    }
}
