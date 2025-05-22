package dk.easv.belmanqcreport.BE;

import dk.easv.belmanqcreport.DAL.Interface.Position;
import dk.easv.belmanqcreport.DAL.Interface.ValidationType;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

import java.io.File;

public class MyImage {

    

    private int orderID;

    private int    imageID;
    private String imagePath;
    private String comment;
    private File imageFile;
    private int imagePositionID;
    private String imagePosition;
    private Position position;
    private int validationTypeID;
    private ValidationType validationType;

    public MyImage(int imagePositionID) {
        this.imagePositionID = imagePositionID;
    }

    public MyImage(ValidationType validationType) {
        this.validationType = validationType;
    }



    public MyImage(File file, Position position) {

        this.imageFile = file;
        this.imagePath = file.getPath();
        this.comment = "";
        this.position = position;

    }

    //called by MyImageDAO
    public MyImage(int imageID, String imagePath, String comment, int imagePositionID, int validationTypeID) {
        this.imageID   = imageID;
        this.imagePath = imagePath;
        this.imageFile = new File(imagePath);
        this.comment   = comment;
        this.position = Position.fromDbId(imagePositionID);
        this.validationTypeID = validationTypeID;
    }
    //called by OrderDAO
    public MyImage(int imageID, String imagePath, String comment) {
        this.imageID   = imageID;
        this.imagePath = imagePath;
        this.imageFile = new File(imagePath);
        this.comment   = comment;
    }

    //called by CameraHandling
    public MyImage(File file) {
        this.imageFile = file;
        this.imagePath = file.getPath();
        this.comment = "";
    }

    public MyImage() {
        this.imageFile = imageFile;
    }

    public File getImageFile() {
        return imageFile;
    }

    public int getOrderItemID() {
        return orderID;
    }

    public void setOrderItemID(int orderID) {
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

    public ImageView getImageNode() {
        ImageView imageView = new ImageView();
        return imageView;
    }

    public Node getImageView() {
        return imageView();
    }

    private Node imageView() {
        return new ImageView(imageFile.toURI().toString());
    }

    public int getUserID() { return 0; }

    public int setUserID() { return 0; }

    public int getImagePositionID() {
        return position.getDbId();
    }

    public void setImagePositionID(int imagePositionID) {
        this.imagePositionID = imagePositionID;
    }

    public Position getImagePosition() {
        return position;
    }

    public void setImagePosition(Position position) {
        this.position = position;
    }

    public int getValidationTypeID() {
        return validationTypeID;
    }

    public ValidationType getValidationType() {
        return validationType.fromId(this.validationTypeID);
    }

    public void setValidationTypeID(int validationTypeID) {
        this.validationTypeID = validationTypeID;
    }
}
