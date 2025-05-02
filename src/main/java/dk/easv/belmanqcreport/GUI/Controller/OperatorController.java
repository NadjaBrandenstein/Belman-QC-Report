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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import java.net.URL;
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
    @FXML
    private ImageView commentIcon;

    private ImageHandlingModel imageHandlingModel;
    private Order currentOrder;

    private final CameraHandling cameraHandler = new CameraHandling();
    private final List<MyImage> capturedImages = new ArrayList<>();
    private int currentImageIndex = -1;


    @FXML
    private void initialize() {

        btnBack.setText("");
        setButtonIcon(btnBack, "/dk/easv/belmanqcreport/Icons/backbtn.png", 20, 20);
        btnRefresh.setText("");
        setButtonIcon(btnRefresh, "/dk/easv/belmanqcreport/Icons/refreshbtn.png", 20, 20);
        btnLogout.setText("");
        setButtonIcon(btnLogout, "/dk/easv/belmanqcreport/Icons/logout.png", 20, 20);
        btnDelete.setText("");
        setButtonIcon(btnDelete, "/dk/easv/belmanqcreport/Icons/delete.png", 30, 30);
        btnPrevious.setText("");
        setButtonIcon(btnPrevious, "/dk/easv/belmanqcreport/Icons/previous.png", 50, 50);
        btnNext.setText("");
        setButtonIcon(btnNext, "/dk/easv/belmanqcreport/Icons/next.png", 50, 50);
        btnCamera.setText("");
        setButtonIcon(btnCamera, "/dk/easv/belmanqcreport/Icons/camera.png", 50, 50);
        btnSave.setText("");
        setButtonIcon(btnSave, "/dk/easv/belmanqcreport/Icons/save.png", 50, 50);

        imageHandlingModel = new ImageHandlingModel();

        try{
            List<Order> orders = imageHandlingModel.getAllOrders();
            if(!orders.isEmpty()){
                currentOrder = orders.get(0);
                //setOrderImage(currentOrder.getImagePath());
                showOrderDetails(currentOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //imageHboxCenter.setOnMouseClicked(event -> openImageHandlingScene());
    }

    private void setOrderImage(String imagePath) {


        File file = new File(imagePath);
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

    private void openImageHandlingScene(MyImage image) {
        try{
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/belmanqcreport/FXML/ImageHandling.fxml"));
            Parent root = loader.load();
            ImageHandlingController controller = loader.getController();
            controller.setImageDetails(image);


            Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
            Stage stage = (Stage) imageHboxCenter.getScene().getWindow();

            stage.setTitle("Image Handling");
            stage.setMaximized(true);
            stage.setScene(scene);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showOrderDetails(Order order) {
        lblOrderNumber.setText(String.valueOf(order.getOrderID()));

        boolean hasComment = order.getComment() != null
                && !order.getComment().isEmpty();
        commentIcon.setVisible(hasComment);
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
            stage.setMaximized(true);
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
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnDelete(ActionEvent actionEvent) {
        if(currentImageIndex >= 0 && currentImageIndex < capturedImages.size()) {
            capturedImages.remove(currentImageIndex);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Image");
            alert.setHeaderText("Are you sure you want to delete this image?");
            alert.setContentText("This action cannot be undone.");
            alert.showAndWait();

            if(currentImageIndex >= capturedImages.size()) {
                currentImageIndex = capturedImages.size() -1;
            }

            if(capturedImages.isEmpty()) {
                imageHboxCenter.getChildren().clear();
                lblImageCount.setText("0 / 0");
            }
            else {
                showImageAtIndex(currentImageIndex);
                updateImageCountLabel();
            }
        }

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
            myImg.setOrderID(currentOrder.getOrderID());

            capturedImages.add(myImg);
            currentImageIndex = capturedImages.size() -1;
            showImageAtIndex(currentImageIndex);
            updateImageCountLabel();

        });
    }

    public void showImageAtIndex(int index) {
        if(index < 0 || index >= capturedImages.size()) return;

        MyImage img = capturedImages.get(index);
        Image fxImage = new Image(img.toURI());
        ImageView imageView = new ImageView(fxImage);

        imageView.fitWidthProperty().bind(imageHboxCenter.widthProperty());
        imageView.fitHeightProperty().bind(imageHboxCenter.heightProperty());
        imageView.setPreserveRatio(false);

        imageView.setOnMouseClicked(event -> openImageHandlingScene(img));

        imageHboxCenter.getChildren().setAll(imageView);
            /*imageHboxCenter.getChildren().clear();
            imageHboxCenter.getChildren().add(imageView);*/

    }

    public void updateImageCountLabel() {
        lblImageCount.setText((currentImageIndex + 1) + " / " + capturedImages.size());
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
    }

    private void setButtonIcon(Button button, String iconPath, double width, double height) {
        if (button == null) {
            System.out.println("Button is null. Cannot set icon: " + iconPath);
            return;
        }

        URL iconUrl = getClass().getResource(iconPath);
        if (iconUrl == null) {
            System.out.println("Error loading icon: " + iconPath);
            return;
        }

        Image icon = new Image(iconUrl.toExternalForm());
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        button.setGraphic(imageView);
    }

    public void setUserName(String userName) {
        lblEmployee.setText(userName);
    }

}