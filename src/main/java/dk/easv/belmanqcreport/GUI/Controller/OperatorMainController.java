package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.BLL.UTIL.CameraHandling;
import dk.easv.belmanqcreport.BLL.UTIL.FXMLNavigator;
import dk.easv.belmanqcreport.DAL.Database.ImageRepository;
import dk.easv.belmanqcreport.DAL.Interface.Position;
import dk.easv.belmanqcreport.GUI.Model.ImageHandlingModel;
import dk.easv.belmanqcreport.GUI.Model.ImageModel;
// Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.opencv.core.Core;
// JavaFx Imports
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
// Java Imports
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class OperatorMainController {

    @FXML
    private Label lblOrderNumber;
    @FXML
    private Label lblEmployee;
    @FXML
    private Label lblImageCount;
    @FXML
    private AnchorPane imageFront;
    @FXML
    private AnchorPane imageBack;
    @FXML
    private AnchorPane imageLeft;
    @FXML
    private AnchorPane imageRight;
    @FXML
    private AnchorPane imageTop;
    @FXML
    private AnchorPane imageExtra;
    @FXML
    private MFXButton btnBack;
    @FXML
    private MFXButton btnLogout;
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
    private ComboBox<OrderItem> cbOrderNumber;

    private ImageHandlingModel imageHandlingModel;
    private ImageModel imageModel;

    private Order currentOrder;
    private OrderItem currentOrderItem;

    private final CameraHandling cameraHandler = new CameraHandling();
    private List<MyImage> capturedImages = new ArrayList<>();
    private int currentImageIndex = -1;
    private Stage stage;


    private final Map<Position, MyImage> imagesByPosition = new EnumMap<>(Position.class);

    private Map<Position, AnchorPane> getPaneByPosition;

    @FXML
    private void initialize() throws Exception {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        setImageViewIcon(logoImage, "/dk/easv/belmanqcreport/Icons/Belman.png");

        btnBack.setText("");
        setButtonIcon(btnBack, "/dk/easv/belmanqcreport/Icons/backbtn.png", 20, 20);
        btnLogout.setText("");
        setButtonIcon(btnLogout, "/dk/easv/belmanqcreport/Icons/logout.png", 20, 20);
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

        getPaneByPosition = Map.of(
                Position.TOP, imageTop,
                Position.FRONT, imageFront,
                Position.BACK, imageBack,
                Position.LEFT, imageLeft,
                Position.RIGHT, imageRight,
                Position.EXTRA, imageExtra
        );

        imageTop.setOnMouseClicked(event -> handleImageClick(Position.TOP));
        imageFront.setOnMouseClicked(event -> handleImageClick(Position.FRONT));
        imageBack.setOnMouseClicked(event -> handleImageClick(Position.BACK));
        imageLeft.setOnMouseClicked(event -> handleImageClick(Position.LEFT));
        imageRight.setOnMouseClicked(event -> handleImageClick(Position.RIGHT));
        imageExtra.setOnMouseClicked(event -> handleImageClick(Position.EXTRA));

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
            stage.initOwner(imageFront.getScene().getWindow());
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



    private void handleImageClick(Position position) {
        if (position == Position.EXTRA) {
            List<MyImage> extras = getExtraImages();
            if (!extras.isEmpty()) {
                showExtraImageAtIndex(currentImageIndex >= 0 ? currentImageIndex : 0);
            } else {
                openCameraAndSaveImage(position);
            }
            return;
        }

        MyImage existingImage = imagesByPosition.get(position);
        if (existingImage != null) {
            openImageHandlingScene(existingImage);
        } else {
            openCameraAndSaveImage(position);
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

    /*@FXML
    private void btnDelete(ActionEvent actionEvent) {
        if (currentImageIndex < 0 || currentImageIndex >= capturedImages.size())
            return;

        MyImage imageToDelete = capturedImages.get(currentImageIndex);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Image");
        alert.setHeaderText("Are you sure you want to delete this image?");
        alert.setContentText("This action cannot be undone.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        File file = imageToDelete.getImageFile();
        if(imageToDelete.getImageID() <= 0) {
            if (file != null && file.exists()) {
                try{
                    if(!file.delete()) {
                        System.out.println("Failed to delete image file: " + file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        if(imageToDelete.getImageID() > 0) {
            try {
                ImageRepository repo =new ImageRepository();
                repo.delete(imageToDelete);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
            //alert.showAndWait();
            capturedImages.remove(currentImageIndex);
            if(currentImageIndex >= capturedImages.size()) {
                currentImageIndex = capturedImages.size() -1;
            }

            if(capturedImages.isEmpty()) {
                imageFront.getChildren().clear();
                lblImageCount.setText("0 / 0");
            }
            else {
                showImageAtIndex(currentImageIndex);
                updateImageCountLabel();
            }

    }*/

    @FXML
    private void btnPrevious(ActionEvent actionEvent) {
        List<MyImage> extraImages = getExtraImages();
        if(currentImageIndex > 0 && currentImageIndex < extraImages.size()) {
            currentImageIndex--;
            showExtraImageAtIndex(currentImageIndex);
            updateImageCountLabel();
        }

    }

    @FXML
    private void btnNext(ActionEvent actionEvent) {
        List<MyImage> extraImages = getExtraImages();
        if(currentImageIndex < extraImages.size() -1) {
            currentImageIndex++;
            showExtraImageAtIndex(currentImageIndex);
            updateImageCountLabel();
        }

    }

    private List<MyImage> getExtraImages(){
        List<MyImage> extraImg = new ArrayList<>();

        for (MyImage img : capturedImages) {
            if (img.getImagePosition() == Position.EXTRA) {
                extraImg.add(img);
            }
        }
        return extraImg;
    }

    private void showExtraImageAtIndex (int index) {
        List<MyImage> extraImages = getExtraImages();
        if(index < 0 || index >= extraImages.size()) {
            return;
        }

        MyImage img = extraImages.get(index);
        Image fxImage = new Image(img.toURI());
        ImageView imageView = new ImageView(fxImage);

        imageView.fitWidthProperty().bind(imageExtra.widthProperty());
        imageView.fitHeightProperty().bind(imageExtra.heightProperty());
        imageView.setPreserveRatio(true);
        imageView.setOnMouseClicked(e -> openImageHandlingScene(img));

        imageExtra.getChildren().setAll(imageView);

    }

    public void updateImageCountLabel() {
        List<MyImage> extraImages = getExtraImages();
        boolean hasExtras = !extraImages.isEmpty();

        lblImageCount.setVisible(hasExtras);
        btnNext.setVisible(hasExtras);
        btnPrevious.setVisible(hasExtras);

        if(extraImages.isEmpty()) {
            lblImageCount.setText("");
        }
        else {
            lblImageCount.setText((currentImageIndex + 1) + " / " + extraImages.size());
        }
    }


    @FXML
    private void btnCamera(ActionEvent actionEvent) {

        // Only allow capturing images for the EXTRA position via this button
        Position position = Position.EXTRA;

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
            controller.setImagePosition(position);

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

    private void showImageForPosition(Position position) {
        AnchorPane targetPane = getPaneByPosition.get(position);
        targetPane.getChildren().clear();

        MyImage image = imagesByPosition.get(position);
        if (image != null) {

            Image fxImage = new Image(image.toURI());
            ImageView imageView = new ImageView(fxImage);

            imageView.fitWidthProperty().bind(targetPane.widthProperty());
            imageView.fitHeightProperty().bind(targetPane.heightProperty());
            imageView.setPreserveRatio(true);
            //imageView.setOnMouseClicked(e -> openImageHandlingScene(image));

            imageView.setOnMouseClicked(null);
            imageView.setOnMouseClicked(e -> {
                e.consume();
                openImageHandlingScene(image);
            });

            targetPane.getChildren().setAll(imageView);
        }
        else {
            targetPane.setOnMouseClicked(e -> openCameraAndSaveImage(position));
        }
    }

    private void openCameraAndSaveImage(Position position) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/belmanqcreport/FXML/Camera.fxml"));
            Scene scene = new Scene(loader.load());

            Stage camStage = new Stage();
            camStage.getIcons().add(new Image("/dk/easv/belmanqcreport/Icons/Belman.png"));
            camStage.setTitle("Capture - " + position);
            camStage.setScene(scene);
            camStage.setResizable(true);
            camStage.setMaximized(true);

            System.out.println("DEBUG: openCameraAndSaveImage() → position: " + position);

            CameraController controller = loader.getController();
            controller.setParentController(this);
            controller.setOrderItem(currentOrderItem);
            controller.setImagePosition(position); // NEW: pass position if needed

            camStage.setOnCloseRequest(event -> controller.cleanup());
            camStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open camera.");
            alert.showAndWait();
        }
    }

    public void showImageAtIndex(int index) {

        MyImage img = capturedImages.get(index);
        Image fxImage = new Image(img.toURI());
        ImageView imageView = new ImageView(fxImage);

        imageView.fitWidthProperty().bind(imageFront.widthProperty());
        imageView.fitHeightProperty().bind(imageFront.heightProperty());
        imageView.setPreserveRatio(false);

        imageView.setOnMouseClicked(event -> openImageHandlingScene(img));

        imageFront.getChildren().setAll(imageView);

    }

    public void displayCapturedImage(MyImage myImg) {

        Platform.runLater(() -> {
            Position pos = myImg.getImagePosition();
            myImg.setOrderItemID(currentOrderItem.getOrderItemId());

            capturedImages.add(myImg);
            currentImageIndex = capturedImages.size() - 1;

            if(pos == Position.EXTRA){
                List<MyImage> extraImages = getExtraImages();
                currentImageIndex = extraImages.size() - 1;
                showExtraImageAtIndex(currentImageIndex);
                //showAllExtraImages();
            }
            else {
                imagesByPosition.put(pos, myImg);
                showImageForPosition(pos);
            }

            updateImageCountLabel();
        });

    }

    private void showAllExtraImages() {
        imageExtra.getChildren().clear();

        for (MyImage img : capturedImages) {
            if(img.getImagePosition() == Position.EXTRA) {
                Image fxImage = new Image(img.toURI());
                ImageView imageView = new ImageView(fxImage);
                imageView.fitWidthProperty().bind(imageExtra.widthProperty());
                imageView.fitHeightProperty().bind(imageExtra.heightProperty());
                imageView.setPreserveRatio(true);
                imageView.setOnMouseClicked(e -> openImageHandlingScene(img));
                imageExtra.getChildren().setAll(imageView);
            }
        }
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        // Check for missing required images
        List<Position> requiredPositions = List.of(Position.TOP, Position.FRONT, Position.BACK, Position.LEFT, Position.RIGHT);

        List<String> missingPositions = new ArrayList<>();
        for(Position pos : requiredPositions) {
            if(!imagesByPosition.containsKey(pos) || imagesByPosition.get(pos) == null) {
                missingPositions.add(pos.name());
            }
        }

        if(!missingPositions.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing images.");
            alert.setHeaderText("Required images are missing.");
            alert.setContentText("Please capture the following images beforesaving: \n" +
                    String.join(", ", missingPositions));
            alert.showAndWait();
            return;
        }

        // Proceed with saving only if all required images are present
        try{
            for (MyImage myImage : capturedImages) {
                if(myImage.getImageID() <= 0) {
                    MyImage saved = imageModel.saveNewImage(myImage);
                    myImage.setImageID(saved.getImageID());
                    currentOrderItem.getImages().add(myImage);
                } else {
                    imageModel.updateImage(myImage);
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Successful");
            alert.setHeaderText(null);
            alert.setContentText("Images have been saved successfully.");
            alert.showAndWait();

            FXMLNavigator.getInstance().navigateTo(stage, "dk/easv/belmanqcreport/FXML/OperatorSearch.fxml");

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
            imagesByPosition.clear();
            clearImages();

            for (MyImage image : imgs) {
                Position pos = image.getImagePosition();
                imagesByPosition.put(pos, image);
                showImageForPosition(pos);
            }

            updateImageCountLabel();

            /*capturedImages = new ArrayList<>(imgs);
            currentImageIndex = imgs.isEmpty() ? -1 : 0;
            clearImages();

            if(currentImageIndex >= 0) {
                showImageAtIndex(currentImageIndex);
                updateImageCountLabel();
            } else {
                //imageHboxCenter.getChildren().clear();
                lblImageCount.setText("0 / 0");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearImages() {
        imageTop.getChildren().clear();
        imageFront.getChildren().clear();
        imageBack.getChildren().clear();
        imageLeft.getChildren().clear();
        imageRight.getChildren().clear();
        imageExtra.getChildren().clear();
        lblImageCount.setText("");
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setFirstNameAndLastName(String text) {
        lblEmployee.setText(text);
    }
}