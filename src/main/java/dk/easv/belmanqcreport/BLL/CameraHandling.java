package dk.easv.belmanqcreport.BLL;
// Other Imports
import com.github.sarxos.webcam.Webcam;
// JavaFX Imports
import dk.easv.belmanqcreport.BE.MyImage;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
// Java Imports
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraHandling {

    private Webcam webcam;
    private boolean previewing;

    public void startCamera() {
        webcam = Webcam.getDefault();
        if(webcam != null) {
            webcam.open();
            previewing = true;
        }
    }

    public void stopCamera() {
        previewing = false;
        if(webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }

    public boolean isCameraActive() {
        return webcam != null && webcam.isOpen();
    }

    public Image getCurrentFrame() {
        if(webcam != null && webcam.isOpen()) {
            BufferedImage img = webcam.getImage();
            if(img != null) {
                return SwingFXUtils.toFXImage(img, null);
            }
        }
        return null;
    }


    public MyImage captureImage() {
        //Webcam webcam = Webcam.getDefault();

        if(webcam == null || !webcam.isOpen()) {
            System.out.println("Webcam not ready");
            return null;
        }

        /*webcam.open();
        BufferedImage image = webcam.getImage();
        webcam.close();*/

        try{
            BufferedImage img = webcam.getImage();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "captured_" + timeStamp + ".png";
            File file = new File("images/" + filename);
            //File file = new File(System.getProperty("user.home") + "/captured_" + timeStamp + ".png");
            file.getParentFile().mkdirs();
            ImageIO.write(img, "PNG", file);
            return new MyImage(file);
        }
        catch (IOException e) {
                e.printStackTrace();
        }
        return null;

    }

}
