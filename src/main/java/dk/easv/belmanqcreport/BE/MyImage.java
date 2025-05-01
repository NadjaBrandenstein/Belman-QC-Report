package dk.easv.belmanqcreport.BE;

import java.io.File;

public class MyImage {

    //private ImageView capturedImageView;
    private File imageFile;
    private int orderID;
    private String comment;

    public MyImage(File imageFile) {
        this.imageFile = imageFile;
    }

    public File getImageFile() {
        return imageFile;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public String toURI() {
        return imageFile.toURI().toString();
    }
}
