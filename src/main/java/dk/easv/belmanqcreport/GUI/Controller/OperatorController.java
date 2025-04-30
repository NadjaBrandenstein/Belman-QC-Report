package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BLL.CameraHandling;
import dk.easv.belmanqcreport.GUI.Model.ImageHandlingModel;
import dk.easv.belmanqcreport.Main;
// Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
// JavaFx Imports
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
// Java Imports
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private HBox imageHboxCamera;
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
    private final List<MyImage> capturedImages = new ArrayList<>();
    private int currentImageIndex = -1;


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
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dk/easv/belmanqcreport/FXML/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());
            stage.setTitle("Belman");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnRefresh(ActionEvent actionEvent) {
    }

    @FXML
    private void btnLogout(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dk/easv/belmanqcreport/FXML/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());
            stage.setTitle("Belman");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnDelete(ActionEvent actionEvent) {
    }

    @FXML
    private void btnPrevious(ActionEvent actionEvent) {
        if(currentImageIndex > 0) {
            currentImageIndex--;
            showImageAtIndex(currentImageIndex);
            updateImageCountLabel();
        }

    }

    @FXML
    private void btnNext(ActionEvent actionEvent) {
        if(currentImageIndex < capturedImages.size() -1) {
            currentImageIndex++;
            showImageAtIndex(currentImageIndex);
            updateImageCountLabel();
        }

    }

    @FXML
    private void btnCamera(ActionEvent actionEvent) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/belmanqcreport/FXML/Camera.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Live Camera Preview");
            stage.setScene(scene);

            stage.setResizable(true);
            stage.setMaximized(true);

            CameraController controller = loader.getController();
            controller.setParentController(this);

            controller.initialize();

            stage.setOnCloseRequest(event -> controller.cleanup());
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open camera.");
            alert.showAndWait();
        }
    }

    public void displayCapturedImage (MyImage myImg) {
        Platform.runLater(() -> {

           capturedImages.add(myImg);
           currentImageIndex = capturedImages.size() -1;
           showImageAtIndex(currentImageIndex);
           updateImageCountLabel();

        });
    }

    public void showImageAtIndex(int index) {
        if(index >= 0 && index < capturedImages.size()) {
            MyImage img = capturedImages.get(index);
            Image fxImage = new Image(img.toURI());

            ImageView imageView = new ImageView(fxImage);
            imageView.fitWidthProperty().bind(imageHboxCenter.widthProperty());
            imageView.fitHeightProperty().bind(imageHboxCenter.heightProperty());
            imageHboxCenter.getChildren().clear();
            imageHboxCenter.getChildren().add(imageView);
        }
    }

    public void updateImageCountLabel() {
        lblImageCount.setText((currentImageIndex + 1) + " / " + capturedImages.size());
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
    }

}
