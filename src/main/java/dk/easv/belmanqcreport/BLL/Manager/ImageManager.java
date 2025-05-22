package dk.easv.belmanqcreport.BLL.Manager;

import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.DAL.Database.ImageRepository;

import java.util.List;

public class ImageManager {

    private final ImageRepository imageRepository;

    public ImageManager() throws Exception {
        this.imageRepository = new ImageRepository();
    }

    public MyImage createImage(MyImage img) throws Exception {
        return imageRepository.add(img);
    }

    public void updateImage(MyImage img){
        imageRepository.update(img);
    }

    public void deleteImage(MyImage img){
        imageRepository.delete(img);
    }

    public List<MyImage> getImagesForOrder(int orderID) throws Exception {
        return imageRepository.getImagesByOrderId(orderID);
    }

    public void updateComment(MyImage img) throws Exception {
        imageRepository.updateComment(img);
    }

    public void updateItemStatus(int orderItemID, int validationTypeID) throws Exception {
        imageRepository.updateValidationType(orderItemID, validationTypeID);
    }

    public int getValidationType(int orderItemID) throws Exception {
        return imageRepository.getValidationTypeByOrderItemID(orderItemID);
    }

    public void updateImageStatus(int imageID, int validationTypeID) throws Exception {
        imageRepository.updateImageValidationType(imageID, validationTypeID);
    }

}
