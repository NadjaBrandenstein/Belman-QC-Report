package dk.easv.belmanqcreport.BLL;
// Other Imports
import com.github.sarxos.webcam.Webcam;
// JavaFX Imports
import dk.easv.belmanqcreport.BE.MyImage;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
// Java Imports
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraHandling {

    public MyImage captureImage() {
        Webcam webcam = Webcam.getDefault();
        if(webcam == null) {
            System.out.println("No webcam detected");
            return null;
        }

        webcam.open();
        BufferedImage image = webcam.getImage();
        webcam.close();

        if(image != null) {
            try{
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String filename = "captured_" + timeStamp + ".png";
                //File file = new File("images/" + filename);
                File file = new File(System.getProperty("user.home") + "/captured_" + timeStamp + ".png");
                file.getParentFile().mkdirs();
                ImageIO.write(image, "PNG", file);
                return new MyImage(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

}
