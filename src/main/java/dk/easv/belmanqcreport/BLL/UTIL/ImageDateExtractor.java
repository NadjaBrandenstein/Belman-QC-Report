package dk.easv.belmanqcreport.BLL.UTIL;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.util.Date;

public class ImageDateExtractor {
    public static void main (String[] args) {
        File imageFile = new File("images");
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
            ExifSubIFDDirectory dicrectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (dicrectory != null) {
                Date date = dicrectory.getDateOriginal();
                if (date != null) {
                    System.out.println("Date taken:" + date);
                } else
                    System.out.println("Date taken not found in EXIF metadata");
            }
        } catch (Exception e) {
            System.out.println("Error reading metadata: " + e.getMessage());

        }
    }
}
