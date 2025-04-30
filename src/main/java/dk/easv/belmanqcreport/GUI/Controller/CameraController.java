package dk.easv.belmanqcreport.GUI.Controller;

import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BLL.CameraHandling;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CameraController {

    @FXML
    ImageView preview;

    private final CameraHandling cameraHandler = new CameraHandling();
    private OperatorController parentController;
    private Thread previewThread;

    public void setParentController(OperatorController controller) {
        this.parentController = controller;
    }

    public void initialize() {
        cameraHandler.startCamera();

        Thread previewThread = new Thread (() -> {
            while (cameraHandler.isCameraActive()) {
                Image img = cameraHandler.getCurrentFrame();
                if (img != null) {
                    Platform.runLater(() -> preview.setImage(img));
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
        cameraHandler.stopCamera();
        ((Stage) preview.getScene().getWindow()).close();
    }

    public void cleanup() {
        cameraHandler.stopCamera();
        if(previewThread != null && previewThread.isAlive()) {
            previewThread.interrupt();
        }
    }

}
