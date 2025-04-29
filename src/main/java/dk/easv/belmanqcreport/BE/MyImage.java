package dk.easv.belmanqcreport.BE;

import java.io.File;

public class MyImage {

    //private ImageView capturedImageView;
    private File imageFile;

    public MyImage(File imageFile) {
        this.imageFile = imageFile;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

}
