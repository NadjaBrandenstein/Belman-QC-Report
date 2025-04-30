package dk.easv.belmanqcreport.BE;

public class Order {
    private int orderID;
    private int userID;
    private String imagePath;
    private String comment;

    public Order(int orderID, int userID, String imagePath, String comment) {
        this.orderID = orderID;
        this.userID = userID;
        this.imagePath = imagePath;
        this.comment = comment;
    }

    
    public int getOrderID() {
        return orderID;
    }

    private void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getImagePath() {
        return imagePath;
    }

    private void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getComment() {
        return comment;
    }

    private void setComment(String comment) {
        this.comment = comment;
    }

    public void add(Order order) {
        setOrderID(order.getOrderID());
        setImagePath(order.getImagePath());
        setComment(order.getComment());
    }

    public String getFirstname() {
        return getFirstname().toLowerCase();
    }

    public String getLastname() {
        return getLastname().toLowerCase();
    }
}
