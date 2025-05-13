package dk.easv.belmanqcreport.GUI.Model;

import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BLL.Manager.ImageManager;

import java.util.List;

public class ImageModel {
    private final ImageManager imageManager = new ImageManager();

    public ImageModel() throws Exception {
    }

    public MyImage saveNewImage(MyImage img) throws Exception {
        return imageManager.createImage(img);
    }

    public void updateImage(MyImage img) {
        imageManager.updateImage(img);
    }
    public void deleteImage(MyImage img) {
        imageManager.deleteImage(img);
    }
    public List<MyImage> getImageForOrder(int orderID) throws Exception {
        return imageManager.getImagesForOrder(orderID);
    }
    public void updateComment(MyImage img) throws Exception {
        imageManager.updateComment(img);
    }
}
