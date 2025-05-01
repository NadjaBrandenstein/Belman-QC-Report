package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BLL.CameraHandling;
// JavaFX Imports
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;

public class CameraController {

    @FXML
    ImageView preview;
    @FXML
    private HBox imageHboxCamera;

    private final CameraHandling cameraHandler = new CameraHandling();
    private OperatorController parentController;
    private QcController qcController;
    private Thread previewThread;
    @FXML
    private MFXButton captureBtn;

    public void setParentController(OperatorController controller) {
        this.parentController = controller;
    }

    public void setQcController(QcController controller) {
        this.qcController = controller;
    }

    public void initialize() {
        captureBtn.setText("");
        setButtonIcon(captureBtn, "/dk/easv/belmanqcreport/Icons/camera.png");

        cameraHandler.startCamera();

        Thread previewThread = new Thread (() -> {
            while (cameraHandler.isCameraActive()) {
                Image img = cameraHandler.getCurrentFrame();
                if (img != null) {
                    Platform.runLater(() -> {
                        imageHboxCamera.getChildren().clear();
                        ImageView imageView = new ImageView(img);
                        imageView.setPreserveRatio(true);
                        imageView.fitWidthProperty().bind(imageHboxCamera.widthProperty());
                        imageView.fitHeightProperty().bind(imageHboxCamera.heightProperty());
                        imageHboxCamera.getChildren().add(imageView);
                    });
                }
                try {
                    Thread.sleep(33);
                }
                catch(InterruptedException ignored) { }
            }
        });
        previewThread.setDaemon(true);
        previewThread.start();
    }

    @FXML
    private void captureBtn(ActionEvent actionEvent) {

        MyImage myImg = cameraHandler.captureImage();
        if(myImg != null && parentController != null) {
            parentController.displayCapturedImage(myImg);
        }
        else if (qcController != null ) {
            qcController.displayCapturedImage(myImg);
        }

        cleanup();
        Stage stage = (Stage) imageHboxCamera.getScene().getWindow();
        stage.close();

    }

    public void cleanup() {
        cameraHandler.stopCamera();
        if(previewThread != null && previewThread.isAlive()) {
            previewThread.interrupt();
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
