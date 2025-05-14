package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.BLL.CameraHandling;
// Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
// JavaFX Imports
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
// Java Imports
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class CameraController {

    @FXML
    ImageView preview;
    @FXML
    private MFXButton captureBtn;
    @FXML
    private HBox imageHboxCamera;

    private VideoCapture capture;
    private Timer timer;
    private Mat frame = new Mat();

    private OrderItem orderItem;

    private final CameraHandling cameraHandler = new CameraHandling();
    private OperatorMainController parentController;
    private QcController qcController;

    public void setParentController(OperatorMainController controller) {
        this.parentController = controller;
    }

    public void initialize() {
        captureBtn.setText("");
        setButtonIcon(captureBtn, "/dk/easv/belmanqcreport/Icons/camera.png");

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        startCamera();

    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    private void startCamera() {
        capture = new VideoCapture(0, Videoio.CAP_DSHOW);
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 640);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);
        capture.set(Videoio.CAP_PROP_BRIGHTNESS, 150);

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
                        });
                    }
                }
            }, 0, 33);
        }
        else {
            System.out.println("Failed to open camera");
        }

    }

    @FXML
    private void captureBtn(ActionEvent actionEvent) {

        if (!frame.empty()) {
            // Create output directory inside user home
            String outputDir = "src/main/resources/dk/easv/belmanqcreport/Images";
            new File(outputDir).mkdirs();

            // Full path for the image
            String filename = "captured_" + System.currentTimeMillis() + ".png";
            String fullPath = outputDir + File.separator + filename;

            // Save image to disk
            Imgcodecs.imwrite(fullPath, frame);
            System.out.println("Image saved to: " + fullPath);

            File imageFile = new File(fullPath);
            MyImage image = new MyImage(imageFile);

            if (parentController != null) {
                image.setOrderItemID(orderItem.getOrderItemId());
                parentController.displayCapturedImage(image);
            }
        }

        cleanup();
        Stage stage = (Stage) imageHboxCamera.getScene().getWindow();
        stage.close();

    }

    public void cleanup() {
        if(timer != null) {
            timer.cancel();
        }
        if(capture != null && capture.isOpened()) {
            capture.release();
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
            mat.get(0, 0, bytes);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            image.getRaster().setDataElements(0, 0, width, height, bytes);

            return SwingFXUtils.toFXImage(image, null);
        }
        catch (Exception e) {
            System.out.println("Error converting mat to image" + e.getMessage());
            return null;
        }
    }

    private void setButtonIcon(Button button, String iconPath) {
        URL iconUrl = getClass().getResource(iconPath);
        if (button == null) {
            System.out.println("Error loading icon: " + iconPath);
            return;
        }

        Image icon = new Image(iconUrl.toExternalForm());
        ImageView imageView = new ImageView(icon);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        imageView.setPreserveRatio(true);

        button.setGraphic(imageView);
    }

}
