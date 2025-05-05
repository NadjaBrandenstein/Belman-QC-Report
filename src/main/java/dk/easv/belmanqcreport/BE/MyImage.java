package dk.easv.belmanqcreport.BE;

import java.io.File;

public class MyImage {

    //private ImageView capturedImageView;
    private File imageFile;
    private int orderID;

    private int    imageID;
    private String imagePath;
    private String comment;

    //called by DAO
    public MyImage(int imageID, String imagePath, String comment) {
        this.imageID   = imageID;
        this.imagePath = imagePath;
        this.imageFile = new File(imagePath);
        this.comment   = comment;
    }

    //called by CameraHandling
    public MyImage(File file) {
        this.imageFile = file;
        this.imagePath = file.getAbsolutePath();
        this.comment = "";
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

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
