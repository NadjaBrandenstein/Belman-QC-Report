package dk.easv.belmanqcreport.BLL.UTIL;
// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
// Other Imports
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
// JavaFX Imports
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
// Java Imports
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CameraHandling {

    private static CameraHandling instance;
    private VideoCapture capture;
    private Timer timer;
    private Mat frame;

    public CameraHandling() {}

    public static synchronized CameraHandling getInstance() {
        if(instance == null) {
            instance = new CameraHandling();
        }
        return instance;
    }

    public void startCamera(ImageView preview, HBox imageHboxCamera) {
        capture = new VideoCapture(0, Videoio.CAP_DSHOW);
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 1280);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 720);
        capture.set(Videoio.CAP_PROP_BRIGHTNESS, 150);

        frame = new Mat();

        if(capture.isOpened()) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    if (capture.read(frame)) {
                        Mat flippedFrame = new Mat();
                        Core.flip(frame, flippedFrame, +1);

                        Image imageToShow = mat2Image(frame);
                        Platform.runLater(() -> {
                            if (imageToShow != null) {
                                preview.setImage(imageToShow);
                            }
                            preview.fitWidthProperty().bind(imageHboxCamera.widthProperty());
                            preview.fitHeightProperty().bind(imageHboxCamera.heightProperty());
                            preview.setPreserveRatio(true);
                        });
                    }
                }
            }, 0, 33);
        }
        else {
            System.out.println("Failed to open camera");
        }

    }

    private Image mat2Image(Mat mat) {
        try {
            Mat converted = new Mat();
            Imgproc.cvtColor(mat, converted, Imgproc.COLOR_BGR2RGB);

            int width = mat.width();
            int height = mat.height();
            int channels = mat.channels();

            byte[] bytes = new byte[width * height * channels];
            converted.get(0, 0, bytes);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            image.getRaster().setDataElements(0, 0, width, height, bytes);

            return SwingFXUtils.toFXImage(image, null);
        }
        catch (Exception e) {
            System.out.println("Error converting mat to image" + e.getMessage());
            return null;
        }
    }

    /*public void cleanup() {
        if(timer != null) {
            timer.cancel();
        }
        if(capture != null && capture.isOpened()) {
            capture.release();
        }
    }*/


    /*public Image getCurrentFrame() {
        if(frame != null && !frame.empty()) {
            return mat2Image(frame);
        }
        return null;
    }*/

    public MyImage captureImage(String outputPath) {
        if(frame != null && !frame.empty()) {
            String FileName = "captured_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";
            String fullPath = outputPath + File.separator + FileName;

            File dir = new File(outputPath);
            dir.mkdirs();
            Imgcodecs.imwrite(fullPath, frame);

            return new MyImage(new File(fullPath));
        }
        return null;
    }

    public void stopCamera() {
        if (timer != null) {
            timer.cancel();
        }
        if (capture != null && capture.isOpened()) {
            capture.release();
        }
    }

}
