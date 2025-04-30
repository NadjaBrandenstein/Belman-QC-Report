package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BLL.CameraHandling;
// Other Imports
import dk.easv.belmanqcreport.GUI.Model.ImageHandlingModel;
import io.github.palexdev.materialfx.controls.MFXButton;
// JavaFx Imports
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OperatorController {

    @FXML
    private Label lblOrderNumber;
    @FXML
    private Label lblEmployee;
    @FXML
    private Label lblImageCount;
    @FXML
    private HBox imageHboxCenter;
    @FXML
    private MFXButton btnBack;
    @FXML
    private MFXButton btnRefresh;
    @FXML
    private MFXButton btnLogout;
    @FXML
    private MFXButton btnDelete;
    @FXML
    private MFXButton btnPrevious;
    @FXML
    private MFXButton btnNext;
    @FXML
    private MFXButton btnCamera;
    @FXML
    private MFXButton btnSave;

    private ImageHandlingModel imageHandlingModel;

    private final CameraHandling cameraHandler = new CameraHandling();

    @FXML
    private void initialize() {
        imageHandlingModel = new ImageHandlingModel();

        try{
            List<Order> orders = imageHandlingModel.getAllOrders();
            if(!orders.isEmpty()){
                Order order = orders.get(0);
                setOrderImage(order.getImagePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageHboxCenter.setOnMouseClicked(event -> openImageHandlingScene());
    }

    private void setOrderImage(String imagePath) {

        String baseDirectory = "src/main/resources/Pic";

        File file = new File(baseDirectory + imagePath);
        if (!file.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Image file does not exist." + file.getAbsolutePath());
            alert.showAndWait();
            return;
        }

        Image img = new Image(file.toURI().toString());

        BackgroundImage bgImg = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO, BackgroundSize.AUTO,
                        false, false, true, false
                )
        );
        imageHboxCenter.setBackground(new Background(bgImg));
    }

    private void openImageHandlingScene() {
        try{
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/belmanqcreport/FXML/ImageHandling.fxml"));
            Scene scene = new Scene(loader.load(), screenBounds.getWidth(), screenBounds.getHeight());
            Stage stage = (Stage) imageHboxCenter.getScene().getWindow();
            stage.setTitle("Image Handling");
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnBack(ActionEvent actionEvent) {
    }

    @FXML
    private void btnRefresh(ActionEvent actionEvent) {
    }

    @FXML
    private void btnLogout(ActionEvent actionEvent) {
    }

    @FXML
    private void btnDelete(ActionEvent actionEvent) {
    }

    @FXML
    private void btnPrevious(ActionEvent actionEvent) {
    }

    @FXML
    private void btnNext(ActionEvent actionEvent) {
    }

    @FXML
    private void btnCamera(ActionEvent actionEvent) {

        Stage stage = new Stage();
        BorderPane borderPane = new BorderPane();
        ImageView preview = new ImageView();
        preview.setFitHeight(400);
        preview.setFitWidth(600);
        preview.setPreserveRatio(true);

        Button captureBtn = new Button("Capture");

        borderPane.setCenter(preview);
        borderPane.setBottom(captureBtn);

        Scene scene = new Scene(borderPane, 600, 500);
        stage.setScene(scene);
        stage.setTitle("Live Camera Preview");
        stage.show();

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

        captureBtn.setOnAction(event -> {
            MyImage myImg = cameraHandler.captureImage();
            if (myImg != null) {
                Platform.runLater(() -> {
                    Image fxImage = new Image(myImg.toURI().toString());
                    ImageView imageView = new ImageView(fxImage);
                    imageView.setFitHeight(400);
                    imageView.setFitWidth(600);
                    imageView.setPreserveRatio(true);
                    imageHboxCenter.getChildren().clear();
                    imageHboxCenter.getChildren().add(imageView);
                });
            }
            cameraHandler.stopCamera();
            stage.close();
        });
        stage.setOnCloseRequest(event -> {cameraHandler.stopCamera();});

        /*MyImage image = cameraHandler.captureImage();
        if(image != null) {
            File imageFile = image.getImageFile();
            Image fxImage = new Image(imageFile.toURI().toString());
            ImageView imageView = new ImageView(fxImage);
            imageView.setFitHeight(258);
            imageView.setFitWidth(528);
            imageView.setPreserveRatio(true);
            imageHboxCenter.getChildren().clear();
            imageHboxCenter.getChildren().add(imageView);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to capture image");
            alert.showAndWait();
        }*/


    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
    }

}
