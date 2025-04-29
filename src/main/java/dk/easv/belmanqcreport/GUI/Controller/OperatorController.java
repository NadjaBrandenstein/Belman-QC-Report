package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BLL.CameraHandling;
// Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
// JavaFx Imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;

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

    private final CameraHandling cameraHandler = new CameraHandling();

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
        MyImage image = cameraHandler.captureImage();
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
        }


    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
    }

}
