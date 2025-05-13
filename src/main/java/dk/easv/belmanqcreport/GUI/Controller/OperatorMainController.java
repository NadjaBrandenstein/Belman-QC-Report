package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.BLL.CameraHandling;
import dk.easv.belmanqcreport.BLL.UTIL.FXMLNavigator;
import dk.easv.belmanqcreport.GUI.Model.ImageHandlingModel;
import dk.easv.belmanqcreport.GUI.Model.ImageModel;
// Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import org.opencv.core.Core;
// JavaFx Imports
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
// Java Imports
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OperatorMainController {


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
    @FXML
    private ImageView logoImage;
    @FXML
    private MFXComboBox<OrderItem> cbOrderNumber;

    private ImageHandlingModel imageHandlingModel;
    private ImageModel imageModel;

    private Order currentOrder;
    private OrderItem currentOrderItem;

    private final CameraHandling cameraHandler = new CameraHandling();
    private List<MyImage> capturedImages = new ArrayList<>();
    private int currentImageIndex = -1;
    private Stage stage;

    @FXML
    private void initialize() throws Exception {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        setImageViewIcon(logoImage, "/dk/easv/belmanqcreport/Icons/Belman.png");

        btnBack.setText("");
        setButtonIcon(btnBack, "/dk/easv/belmanqcreport/Icons/backbtn.png", 20, 20);
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
        imageModel = new ImageModel();


        cbOrderNumber.setConverter(new StringConverter<>() {
            @Override public String toString(OrderItem item) {
                return item == null ? "" : item.getOrderItem();
            }
            @Override public OrderItem fromString(String s) { return null; }
        });

        cbOrderNumber.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldItem, newItem) -> {
                    if (newItem != null) {
                        System.out.println("DEBUG: selected OrderItemID=" + newItem.getOrderItemId());
                        currentOrderItem = newItem;
                        loadImagesForItem(newItem.getOrderItemId());
                    }
                });


        /*cbOrderNumber.setOnAction(event -> {
            currentOrderItem = cbOrderNumber.getSelectionModel().getSelectedItem();

            if (currentOrderItem != null) {
                loadImagesForItem(currentOrderItem.getOrderItemId());

            }
        });*/
        //imageHboxCenter.setOnMouseClicked(event -> openImageHandlingScene());
    }







    private void openImageHandlingScene(MyImage image) {
        try{
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/belmanqcreport/FXML/ImageHandling.fxml"));
            Parent root = loader.load();
            ImageHandlingController controller = loader.getController();

            controller.setOrderDetails(currentOrder,
                    image, updatedImage -> {
                capturedImages.set(currentImageIndex, updatedImage);
                showImageAtIndex(currentImageIndex);
                updateImageCountLabel();
            });


            Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
            Stage stage = new Stage();
            stage.initOwner(imageHboxCenter.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Image Handling");
            stage.getIcons().add(new Image("/dk/easv/belmanqcreport/Icons/Belman.png"));

            stage.setMaximized(true);

            stage.setScene(scene);
            stage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private void showOrderDetails(Order order) {
        lblOrderNumber.setText(String.valueOf(order.getOrderID()));

        boolean hasComment = order.getComment() != null
                && !order.getComment().isEmpty();
        commentIcon.setVisible(hasComment);
    }*/

    @FXML
    private void btnBack(ActionEvent actionEvent) {
        FXMLLoader loader = FXMLNavigator.getInstance().navigateTo(stage, "dk/easv/belmanqcreport/FXML/OperatorSearch.fxml");
        if (loader != null) {
            OperatorSearchController controller = loader.getController();
            controller.setUserName(this.lblEmployee.getText());
            controller.setStage(this.stage);
        }
    }

    @FXML
    private void btnLogout(ActionEvent actionEvent) {
        FXMLNavigator.getInstance().navigateTo(stage, "dk/easv/belmanqcreport/FXML/Login.fxml");
    }

    @FXML
    private void btnDelete(ActionEvent actionEvent) {
        if(currentImageIndex >= 0 && currentImageIndex < capturedImages.size()) {
            capturedImages.remove(currentImageIndex);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Image");
            alert.setHeaderText("Are you sure you want to delete this image?");
            alert.setContentText("This action cannot be undone.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }
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
            stage.getIcons().add(new Image("/dk/easv/belmanqcreport/Icons/Belman.png"));
            stage.setTitle("Live Camera Preview");
            stage.setScene(scene);

            stage.setResizable(true);
            stage.setMaximized(true);

            CameraController controller = loader.getController();
            controller.setParentController(this);
            controller.setOrderItem(currentOrderItem);

            //controller.initialize();

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


            myImg.setOrderItemID(currentOrderItem.getOrderId());

            capturedImages.add(myImg);
            currentImageIndex = capturedImages.size() -1;
            updateImageCountLabel();
            showImageAtIndex(currentImageIndex);

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

    }

    public void updateImageCountLabel() {
        lblImageCount.setText((currentImageIndex + 1) + " / " + capturedImages.size());
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        try{
            for (MyImage myImage : capturedImages) {
                if(myImage.getImageID() <= 0) {
                    MyImage saved = imageModel.saveNewImage(myImage);
                    myImage.setImageID(saved.getImageID());
                    currentOrderItem.getImages().add(myImage);
                } else {
                    imageModel.updateImage(myImage);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Save Successful");
                alert.setHeaderText(null);
                alert.setContentText("Images have been saved successfully.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
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

    private void setImageViewIcon(ImageView logoImage, String iconPath) {
        if (logoImage == null) {
            System.out.println("logoImage is null. Cannot set icon: " + iconPath);
            return;
        }

        URL iconUrl = getClass().getResource(iconPath);
        if (iconUrl == null) {
            System.out.println("Error loading icon: " + iconPath);
            return;
        }

        Image icon = new Image(iconUrl.toExternalForm());
        logoImage.setImage(icon);
        logoImage.setFitWidth(100);
        logoImage.setFitHeight(100);
        logoImage.setPreserveRatio(true);
    }

    public void setOrderNumber(String orderNumber) throws Exception {
        lblOrderNumber.setText(orderNumber);

        Optional<Order> opt = imageHandlingModel.findOrderByNumber(orderNumber);
        if (opt.isPresent()) {
            currentOrder = opt.get();
            //showOrderDetails(currentOrder);
        } else {
            new Alert(Alert.AlertType.ERROR, "Could not find order “" + orderNumber + "”")
                    .showAndWait();
            return;
        }


        try {
            // fetch only the items for this one order!
            List<OrderItem> items = imageHandlingModel.getItemsByOrderNumber(orderNumber);

            System.out.println("DEBUG: setOrderNumber("+orderNumber+") → "+items.size()+" items:");
            for (OrderItem oi : items) {
                System.out.printf("    – id=%d, text=%s%n",
                        oi.getOrderItemId(),
                        oi.getOrderItem());
            }

            cbOrderNumber.getItems().setAll(items);

            if (!items.isEmpty()) {
                cbOrderNumber.getSelectionModel().selectFirst();
                loadImagesForItem(items.get(0).getOrderItemId());
            } else {
                // no items → clear images
                clearImages();
                /*imageHboxCenter.getChildren().clear();
                lblImageCount.setText("0 / 0");*/
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load items for order “" + orderNumber + "”: " + ex.getMessage())
                    .showAndWait();
        }
    }

    private void loadImagesForItem(int orderItemID) {
        try{
            List<MyImage> imgs = imageModel.getImageForOrder(orderItemID);

            System.out.println("DEBUG: loadImagesForItem(" + orderItemID + ") → " + imgs.size() + " images");

            capturedImages = new ArrayList<>(imgs);
            currentImageIndex = imgs.isEmpty() ? -1 : 0;
            clearImages();

            if(currentImageIndex >= 0) {
                showImageAtIndex(currentImageIndex);
                updateImageCountLabel();
            } else {
                //imageHboxCenter.getChildren().clear();
                lblImageCount.setText("0 / 0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearImages() {
        imageHboxCenter.getChildren().clear();
    }


    /*@FXML
    private void cbOrderNumber(ActionEvent actionEvent) {

        OrderItem selectedOrder = cbOrderNumber.getSelectedItem();
        if (selectedOrder != null) {
            currentOrderItem = selectedOrder;
            capturedImages = new ArrayList<>(currentOrderItem.getImages());
            currentImageIndex = 0;
            if (!capturedImages.isEmpty()) {
                showImageAtIndex(currentImageIndex);
                updateImageCountLabel();
            } else {
                imageHboxCenter.getChildren().clear();
                lblImageCount.setText("0 / 0");
            }
        }

    }*/

    private void displayImages(List<MyImage> capturedImages) {
        imageHboxCenter.getChildren().clear();
        for (MyImage image : capturedImages) {

            String uri = new File(image .getImagePath()).toURI().toString();
            Image fxImage = new Image(uri);

            ImageView imageView = new ImageView(fxImage);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            imageHboxCenter.getChildren().add(imageView);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }
}