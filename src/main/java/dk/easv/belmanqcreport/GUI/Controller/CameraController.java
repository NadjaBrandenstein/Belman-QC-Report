package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.BLL.UTIL.CameraHandling;
import dk.easv.belmanqcreport.DAL.Interface.Position;
// Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
import org.opencv.core.Core;
// JavaFX Imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
// Java Imports
import java.net.URL;

public class CameraController {

    @FXML
    ImageView preview;
    @FXML
    private MFXButton captureBtn;
    @FXML
    private HBox imageHboxCamera;

    private final CameraHandling cameraHandler = CameraHandling.getInstance();
    private OrderItem orderItem;
    private Position capturePosition;
    private OperatorMainController parentController;
    private QcController qcController;

    public void setParentController(OperatorMainController controller) {
        this.parentController = controller;
    }

    public void initialize() {
        captureBtn.setText("");
        setButtonIcon(captureBtn, "/dk/easv/belmanqcreport/Icons/camera.png");

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        cameraHandler.startCamera(preview, imageHboxCamera);
    }

    @FXML
    private void captureBtn(ActionEvent actionEvent) {
        String outputDir = "src/main/resources/dk/easv/belmanqcreport/Images";
        MyImage image = cameraHandler.captureImage(outputDir);
        if(image != null && parentController != null) {
            image.setOrderItemID(orderItem.getOrderItemId());
            image.setPosition(capturePosition);
            parentController.displayCapturedImage(image);
        }
        cleanup();
        ((Stage) imageHboxCamera.getScene().getWindow()).close();
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    void setImagePosition(Position position) {
        this.capturePosition = position;
    }

    public void cleanup() {
        cameraHandler.stopCamera();
    }

    private void setButtonIcon(Button button, String iconPath) {
        URL iconUrl = getClass().getResource(iconPath);
        if (iconUrl != null) {
            Image icon = new Image(iconUrl.toExternalForm());
            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            imageView.setPreserveRatio(true);

            button.setGraphic(imageView);
        }
    }

}
