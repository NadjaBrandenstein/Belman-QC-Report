package dk.easv.belmanqcreport.BE;
// Project Imports
import dk.easv.belmanqcreport.DAL.Interface.Position;
import dk.easv.belmanqcreport.DAL.Interface.ValidationType;
// Java Imports
import java.io.File;

public class MyImage {

    private int orderID;
    private int imageID;
    private String imagePath;
    private String comment;
    private File imageFile;
    private int imagePositionID;
    private Position position;
    private int validationTypeID;
    private ValidationType validationType;

    public MyImage(int imagePositionID) {
        this.imagePositionID = imagePositionID;
    }

    public MyImage(ValidationType validationType) {
        this.validationType = validationType;
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

    public String toURI() {
        return imageFile.toURI().toString();
    }

    public int getImagePositionID() {
        return position.getDbId();
    }

    public Position getImagePosition() {
        return position;
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

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getFilePath() {
        return "";
    }
}
