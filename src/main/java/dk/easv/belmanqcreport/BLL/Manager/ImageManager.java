package dk.easv.belmanqcreport.BLL.Manager;

import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.DAL.Database.ImageDAO_DB;

import java.util.List;

public class ImageManager {

    private ImageDAO_DB imageDAO_DB;

    public ImageManager() {
        imageDAO_DB = new ImageDAO_DB();
    }

    public MyImage createImage(MyImage img) throws Exception { return imageDAO_DB.createImage(img); }

    public void    updateImage(MyImage img) throws Exception { imageDAO_DB.updateImage(img); }

    public void    deleteImage(MyImage img) throws Exception { imageDAO_DB.deleteImage(img.getImageID()); }

    public List<MyImage> getImagesForOrder(int orderID) throws Exception { return imageDAO_DB.getImagesForOrder(orderID); }

    public void updateComment(MyImage img) throws Exception { imageDAO_DB.updateComment(img); }

}
