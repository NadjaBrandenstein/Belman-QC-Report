package dk.easv.belmanqcreport.GUI.Controller;

// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.BLL.UTIL.CameraHandling;
import dk.easv.belmanqcreport.BLL.UTIL.ImageDataFetcher;
import dk.easv.belmanqcreport.BLL.UTIL.PDFGenerator;
import dk.easv.belmanqcreport.BLL.UTIL.PDFGeneratorImp;
import dk.easv.belmanqcreport.DAL.Interface.Position;
import dk.easv.belmanqcreport.GUI.Model.ImageHandlingModel;
import dk.easv.belmanqcreport.GUI.Model.ImageModel;
import dk.easv.belmanqcreport.Main;
//Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
// JavaFX Imports
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.stage.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
// Java Imports
import javax.imageio.ImageIO;
import javax.swing.text.Document;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class QcController implements Initializable {

    @FXML
    private Label lblEmployee;
    @FXML
    private Label lblImageCount;
    @FXML
    private StackPane imageFront;
    @FXML
    private StackPane imageBack;
    @FXML
    private StackPane imageLeft;
    @FXML
    private StackPane imageRight;
    @FXML
    private StackPane imageTop;
    @FXML
    private StackPane imageExtra;
    @FXML
    private MFXButton btnBack;
    @FXML
    private MFXButton btnLogout;
    @FXML
    private MFXButton btnPrevious;
    @FXML
    private MFXButton btnNext;
    @FXML
    private MFXButton btnPDFSave;
    @FXML
    private ImageView logoImage;
    @FXML
    private MFXCheckbox idApproved;
    @FXML
    private MFXCheckbox idDenied;

    @FXML
    private ListView<Order> lstOrder;
    @FXML
    private ListView<OrderItem> lstItem;
    @FXML
    private ListView<String> lstLog;
    private final ObservableList<String> logItems = FXCollections.observableArrayList();

    private ImageHandlingModel imageHandlingModel;
    private ImageModel imageModel;
    private Order order;
    private Window primaryStage;
    private int imagePositionID;

    private final CameraHandling cameraHandler = new CameraHandling();
    private List<MyImage> capturedImages = new ArrayList<>();
    private int currentImageIndex = -1;

    private final Set<OrderItem> deniedItems = new HashSet<>();
    private final Set<OrderItem> approvedItems = new HashSet<>();

    private final Map<Position, MyImage> imagesByPosition = new EnumMap<>(Position.class);
    private final Map<Position, Rectangle> imagePanesOverlay = new EnumMap<>(Position.class);
    private Map<Position, StackPane> getPaneByPosition;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        imageHandlingModel = new ImageHandlingModel();

        try {
            imageModel = new ImageModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        setIcons();

        lstLog.setItems(logItems);

        initializeCheckBoxes();

        try {
            populateLists();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getPaneByPosition = Map.of(
                Position.TOP, imageTop,
                Position.FRONT, imageFront,
                Position.BACK, imageBack,
                Position.LEFT, imageLeft,
                Position.RIGHT, imageRight,
                Position.EXTRA, imageExtra
        );







    }

    private void initializeCheckBoxes(){

        setButtonIcon(btnPDFSave, "/dk/easv/belmanqcreport/Icons/pdf.png", 50, 50);
        Node pdfIcon = btnPDFSave.getGraphic();
        btnPDFSave.setGraphic(null);
        btnPDFSave.setText("");

        idApproved.selectedProperty().addListener((obs, oldId, newId) -> {
            if (newId) {
                idDenied.setSelected(false);
            }
        });
        idDenied.selectedProperty().addListener((obs, oldId, newId) -> {
            if (newId) {
                idApproved.setSelected(false);
            }
        });

        btnPDFSave.graphicProperty().bind(
                Bindings.when(idApproved.selectedProperty())
                        .then(pdfIcon)
                        .otherwise((Node) null)
        );

        btnPDFSave.textProperty().bind(
                Bindings.when(idDenied.selectedProperty())
                        .then("Deny")
                        .otherwise("")
        );

    }

    private void populateLists() throws Exception {

        List<Order> orders;

        orders = imageHandlingModel.getAllOrders();

        lstOrder.getItems().setAll(orders);

        lstOrder.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Order o, boolean empty) {
                super.updateItem(o, empty);
                setText(empty||o==null ? null : o.getOrderNumber());
            }
        });

        lstOrder.getSelectionModel().selectedItemProperty().addListener((obs, oldOrder, selOrder) -> {
            if(selOrder != null) {
                this.order = selOrder;
                try {
                    List<OrderItem> items = imageHandlingModel.getItemsByOrderID(selOrder.getOrderID());
                    lstItem.getItems().setAll(items);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                lstItem.getItems().clear();
            }
        });

        lstItem.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem != null){

                    loadImagesForItem(newItem.getOrderItemId());
            } else {
                clearImages();
                lblImageCount.setText("0 / 0");
            }
        });

        lstItem.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(OrderItem item, boolean empty){
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.getOrderItem());
                    if(deniedItems.contains(item)) {
                        setStyle("-fx-background-color: red;");
                    } else if(approvedItems.contains(item)){
                        setStyle("-fx-background-color: green;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

    }

    private void loadImagesForItem(int orderItemId) {
        try{
            List<MyImage> images = imageModel.getImageForOrder(orderItemId);

            imagesByPosition.clear();

            clearImages();
            for (StackPane pane : getPaneByPosition.values()) {
                pane.setStyle("");   // reset background
            }

            for(MyImage image : images){
                Position position = image.getImagePosition();
                imagesByPosition.put(position, image);
                showImageForPosition(position);
            }

            lblImageCount.setText(imagesByPosition.size() + " / " + Position.values().length);

            OrderItem selectedItem = lstItem.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if(approvedItems.contains(selectedItem)) {
                    applyOverlayForItem(selectedItem, true);
                } else if (deniedItems.contains(selectedItem)) {
                    applyOverlayForItem(selectedItem, false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showImageForPosition(Position position) {
        StackPane cell = getPaneByPosition.get(position);
        cell.getChildren().clear();

        MyImage image = imagesByPosition.get(position);
        if (image != null) {

            ImageView imageView = new ImageView(new Image(image.toURI()));
            imageView.fitWidthProperty().bind(cell.widthProperty());
            imageView.fitHeightProperty().bind(cell.heightProperty());
            imageView.setPreserveRatio(true);


            Rectangle overlay = new Rectangle();
            overlay.widthProperty().bind(cell.widthProperty());
            overlay.heightProperty().bind(cell.heightProperty());
            overlay.setFill(Color.color(0, 0, 0, 0));
            overlay.setMouseTransparent(true);

            imageView.setOnMouseClicked(e -> openImageHandlingScene(image));

            cell.getChildren().addAll(imageView, overlay);
            imagePanesOverlay.put(position, overlay);
        }
    }



    private void clearImages() {
        imageTop.getChildren().clear();
        imageFront.getChildren().clear();
        imageBack.getChildren().clear();
        imageLeft.getChildren().clear();
        imageRight.getChildren().clear();
        imageExtra.getChildren().clear();
    }



    private void setIcons(){

        setImageViewIcon(logoImage, "/dk/easv/belmanqcreport/Icons/Belman.png");
        btnBack.setText("");
        setButtonIcon(btnBack, "/dk/easv/belmanqcreport/Icons/backbtn.png", 20, 20);
        btnLogout.setText("");
        setButtonIcon(btnLogout, "/dk/easv/belmanqcreport/Icons/logout.png", 20, 20);
        btnPrevious.setText("");
        setButtonIcon(btnPrevious, "/dk/easv/belmanqcreport/Icons/previous.png", 50, 50);
        btnNext.setText("");
        setButtonIcon(btnNext, "/dk/easv/belmanqcreport/Icons/next.png", 50, 50);


    }

    public void btnBack(ActionEvent actionEvent) {
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

    public void btnLogout(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dk/easv/belmanqcreport/FXML/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());
            stage.getIcons().add(new Image("/dk/easv/belmanqcreport/Icons/Belman.png"));
            stage.setTitle("Belman");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnPrevious(ActionEvent actionEvent) {
        if (currentImageIndex > 0) {
            currentImageIndex--;
            showImageAtIndex(currentImageIndex);
            updateImageCountLabel();
        }
    }

    public void btnNext(ActionEvent actionEvent) {
        if (currentImageIndex < capturedImages.size() - 1) {
            currentImageIndex++;
            showImageAtIndex(currentImageIndex);
            updateImageCountLabel();
        }
    }

    public void showImageAtIndex(int index) {


        if(index < 0 || index >= capturedImages.size()) return;

        MyImage img = capturedImages.get(index);
        Image fxImage = new Image(img.toURI());
        ImageView imageView = new ImageView(fxImage);

        imageView.fitWidthProperty().bind(imageFront.widthProperty());
        imageView.fitHeightProperty().bind(imageFront.heightProperty());
        imageView.setPreserveRatio(false);

        imageView.setOnMouseClicked(event -> openImageHandlingScene(img));

        imageFront.getChildren().setAll(imageView);
    }



    public void openImageHandlingScene(MyImage image) {
        try{
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/belmanqcreport/FXML/ImageHandling.fxml"));
            Parent root = loader.load();
            ImageHandlingController controller = loader.getController();

            controller.setOrderDetails(order,
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

    public void updateImageCountLabel() {
        lblImageCount.setText((currentImageIndex + 1) + " / " + capturedImages.size());
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


    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }



    private PDFGeneratorImp pdfGenerator;

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        OrderItem selected = lstItem.getSelectionModel().getSelectedItem();
        pdfGenerator = PDFGeneratorImp.getInstance();
        dk.easv.belmanqcreport.BLL.UTIL.OrderItem utilOrderItem = new dk.easv.belmanqcreport.BLL.UTIL.OrderItem();
        utilOrderItem.setItemNumber(selected.getOrderItem());
        pdfGenerator.setOrderItem(utilOrderItem);
        String user = lblEmployee.getText();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No order item selected.");
            return;
        }


        boolean isDeny = idDenied.isSelected();
        boolean isApprove = idApproved.isSelected();

        if (isDeny == isApprove) {
            showAlert(Alert.AlertType.WARNING, "Please select either Approve or Deny.");
            return;
        }

        String verb = isDeny ? "Deny" : "Approve";
        if (!showConfirmation("Confirm " + verb, "Are you sure you want to " + verb + " item? " + selected.getOrderItem() + "?")) {
            resetCheckBoxes();
            return;
        }

        if (isDeny) {
            updateItemStatus(selected, deniedItems, approvedItems, "Denied", user);
        } else {
            updateItemStatus(selected, approvedItems, deniedItems, "Approved", user);
            savePDF(actionEvent);
        }
    }

    private void updateItemStatus(OrderItem item, Set<OrderItem> addTo, Set<OrderItem> removeFrom, String status, String user) {
        if (status.equals("Approved")) {
            //item.setOrderItem(item.getOrderItem() + " (Approved)");
        }else if(status.equalsIgnoreCase("Denied")){
            //item.setOrderItem(item.getOrderItem() + " (Denied)");
        }
        addTo.add(item);
        removeFrom.remove(item);
        //item.setOrderItem("NewOrderItem: " + item.getOrderItem());
        lstItem.refresh();
        showInfo("Item “" + item.getOrderItem() + "” has been " + status.toLowerCase() + ".");
        logItems.add(String.format("%s item %s by %s", status, item.getOrderItem(), user));
        lstLog.scrollTo(logItems.size() - 1);
        resetCheckBoxes();
        applyOverlayForItem(item, status.equals("Approved"));
    }

    private void applyOverlayForItem(OrderItem selected, boolean approved) {
        // clear all overlays
        imagePanesOverlay.values().forEach(r -> r.setFill(Color.color(0,0,0,0)));
        // tint only matching slot(s)
        imagesByPosition.forEach((pos, img) -> {
            if (img.getOrderItemID() == selected.getOrderItemId()) {
                Rectangle overlay = imagePanesOverlay.get(pos);
                overlay.setFill(
                        approved
                                ? Color.color(0,1,0, 0.3)   // transparent green
                                : Color.color(1,0,0, 0.3)   // transparent red
                );
            }
        });
    }

    private void savePDF(ActionEvent actionEvent) {
        try {
            File pdfFile = showSaveDialog("Report.pdf");
            if (pdfFile != null) {
                PDFGeneratorImp pdfGen = PDFGeneratorImp.getInstance();
                pdfGen.setEmployeeName(lblEmployee.getText());
                pdfGen.generatePDF(pdfFile.getAbsolutePath(), capturedImages);
                System.out.println("PDF saved to " + pdfFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File showSaveDialog(String initialFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        fileChooser.setInitialFileName(initialFileName);
        return fileChooser.showSaveDialog((Stage) btnPDFSave.getScene().getWindow());
    }

    private boolean showConfirmation(String title, String content) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle(title);
        return confirm.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }

    private void resetCheckBoxes() {
        idDenied.setSelected(false);
        idApproved.setSelected(false);
    }








    private List<MyImage> getImageList() {
        return List.of();
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
        logoImage.setFitWidth(100);  // Set your desired width
        logoImage.setFitHeight(100); // Set your desired height
        logoImage.setPreserveRatio(true);
    }


    private void displayImages(List<MyImage> capturedImages) {
        imageFront.getChildren().clear();
        for (MyImage image : capturedImages) {

            String uri = new File(image .getImagePath()).toURI().toString();
            Image fxImage = new Image(uri);

            ImageView imageView = new ImageView(fxImage);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);

            imageFront.getChildren().add(imageView);
        }
    }

    @FXML
    private void checkApproved(ActionEvent actionEvent) {
    }

    @FXML
    private void checkDenied(ActionEvent actionEvent) {
    }

    public void setFirstNameAndLastName(String text) {
        lblEmployee.setText(text);
    }
}
