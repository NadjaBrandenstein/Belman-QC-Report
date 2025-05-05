package dk.easv.belmanqcreport.DAL.Interface;

import dk.easv.belmanqcreport.BE.MyImage;

import java.util.List;

public interface IImage {
    MyImage createImage(MyImage image) throws Exception;

    void updateImage(MyImage img) throws Exception;

    void deleteImage(int imageID) throws Exception;

    List<MyImage> getImagesForOrder(int orderID) throws Exception;
}
