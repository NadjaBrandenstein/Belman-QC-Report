package dk.easv.belmanqcreport.GUI.Controller;

// Project Imports
import dk.easv.belmanqcreport.BE.Log;
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.BLL.UTIL.CameraHandling;
import dk.easv.belmanqcreport.BLL.UTIL.PDFGeneratorImp;
import dk.easv.belmanqcreport.DAL.Interface.Position;
import dk.easv.belmanqcreport.DAL.Interface.ValidationType;
import dk.easv.belmanqcreport.GUI.Model.ImageHandlingModel;
import dk.easv.belmanqcreport.GUI.Model.ImageModel;
import dk.easv.belmanqcreport.GUI.Model.LogModel;
import dk.easv.belmanqcreport.Main;
//Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;

// JavaFX Imports
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.stage.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
// Java Imports

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
    private MFXCheckbox idApproveAll;
    @FXML
    private MFXCheckbox idDenyAll;

    @FXML
    private ListView<Order> lstOrder;
    @FXML
    private ListView<OrderItem> lstItem;
    @FXML
    private ListView<String> lstLog;
    @FXML
    private TableView<Order> orderTableView;

    private final ObservableList<String> logItems = FXCollections.observableArrayList();
    private PDFGeneratorImp pdfGenerator;

    private ImageHandlingModel imageHandlingModel;
    private ImageModel imageModel;
    private LogModel logModel;
    private Order order;
    private Window primaryStage;
    private int imagePositionID;

    private final CameraHandling cameraHandler = new CameraHandling();
    private List<MyImage> capturedImages = new ArrayList<>();
    private int currentImageIndex = -1;

    private final Set<OrderItem> deniedItems = new HashSet<>();
    private final Set<OrderItem> approvedItems = new HashSet<>();
    private boolean batchApproveSelected = false;
    private boolean batchDenySelected    = false;

    private final Map<Position, MyImage> imagesByPosition = new EnumMap<>(Position.class);
    private final Map<Position, Rectangle> imagePanesOverlay = new EnumMap<>(Position.class);
    private Map<Position, StackPane> getPaneByPosition;
    private String orderNumber;
    private String orderItem;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        imageHandlingModel = new ImageHandlingModel();
        try {
            logModel = new LogModel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

        btnPrevious.setVisible(false);
        btnNext.setVisible(false);
        lblImageCount.setVisible(false);

    }

    private void initializeCheckBoxes() {

        setButtonIcon(btnPDFSave, "/dk/easv/belmanqcreport/Icons/pdf.png", 50, 50);
        Node pdfIcon = btnPDFSave.getGraphic();
        btnPDFSave.setGraphic(null);
        btnPDFSave.setText("");

        idApproveAll.selectedProperty().addListener((obs, oldId, newId) -> {
            batchApproveSelected = newId;
            if (newId) {
                idDenyAll.setSelected(false);
            }
        });
        idDenyAll.selectedProperty().addListener((obs, oldId, newId) -> {
            batchDenySelected = newId;
            if (newId) {
                idApproveAll.setSelected(false);
            }
        });

        btnPDFSave.graphicProperty().bind(
                Bindings.when(idApproveAll.selectedProperty())
                        .then(pdfIcon)
                        .otherwise((Node) null)
        );

        btnPDFSave.textProperty().bind(
                Bindings.when(idDenyAll.selectedProperty())
                        .then("Send back")
                        .otherwise("")
        );


    }

    private void markAllImages(boolean approve) {
        int validationTypeID = approve
                ? ValidationType.APPROVED.getId()
                : ValidationType.DENIED.getId();

        imagesByPosition.values().forEach(img -> {

            try {
                imageModel.updateImageStatus(img.getImageID(), validationTypeID);

            img.setValidationTypeID(validationTypeID);
            Rectangle overlay = imagePanesOverlay.get(img.getImagePosition());
            overlay.setFill(approve
                    ? Color.color(0, 1, 0, 0.3)
                    : Color.color(1, 0, 0, 0.3));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void populateLists() throws Exception {

        List<Order> orders;

        orders = imageHandlingModel.getAllOrders();

        lstOrder.getItems().setAll(orders);

        lstOrder.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Order o, boolean empty) {
                super.updateItem(o, empty);
                setText(empty || o == null ? null : o.getOrderNumber());
            }
        });

        lstOrder.getSelectionModel().selectedItemProperty().addListener((obs, oldOrder, selOrder) -> {
            if (selOrder != null) {
                this.order = selOrder;
                try {
                    List<OrderItem> items = imageHandlingModel.getItemsByOrderID(selOrder.getOrderID());
                    lstItem.getItems().setAll(items);

                    //fetching from db
                    for (OrderItem item : lstItem.getItems()) {
                        int validType = imageModel.getValidationType(item.getOrderItemId());
                        if (validType == ValidationType.APPROVED.getId()) {
                            approvedItems.add(item);
                        } else if (validType == ValidationType.DENIED.getId()) {
                            deniedItems.add(item);
                        }
                    }
                    lstItem.refresh(); //

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                lstItem.getItems().clear();
            }
        });

        lstItem.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {

                loadImagesForItem(newItem.getOrderItemId());
                try {
                    loadLogList(newItem.getOrderItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                clearImages();
                logItems.clear();
                lblImageCount.setText("0 / 0");
            }
        });

        lstItem.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(OrderItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(item.getOrderItem());

                try {
                    List<MyImage> images = imageModel.getImageForOrder(item.getOrderItemId());

                    if (images.isEmpty()) {
                        setStyle("");
                    } else {

                        boolean allApproved = images.stream()
                                .allMatch(img -> img.getValidationTypeID() == ValidationType.APPROVED.getId());
                        boolean allDenied = images.stream()
                                .allMatch(img -> img.getValidationTypeID() == ValidationType.DENIED.getId());

                        if(allApproved) {
                            setStyle("-fx-background-color: green;");
                        } else if (allDenied) {
                            setStyle("-fx-background-color: red;");
                        } else {
                            setStyle("");
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private void loadImagesForItem(int orderItemId) {
        try {
            List<MyImage> images = imageModel.getImageForOrder(orderItemId);

            imagesByPosition.clear();

            clearImages();
            for (StackPane pane : getPaneByPosition.values()) {
                pane.setStyle("");   // reset background
            }

            for (MyImage image : images) {
                Position position = image.getImagePosition();
                imagesByPosition.put(position, image);
                showImageForPosition(position);
            }

            imagesByPosition.forEach((pos, img) -> {
                Rectangle overlay = imagePanesOverlay.get(pos);
                switch (img.getValidationType()) {
                    case APPROVED:
                        overlay.setFill(Color.color(0,1,0,0.3));
                        break;
                    case DENIED:
                        overlay.setFill(Color.color(1,0,0,0.3));
                        break;
                    default:
                        overlay.setFill(Color.color(0,0,0,0));
                }
            });

            List<MyImage> extraImages = images.stream()
                    .filter(img -> img.getImagePosition() == Position.EXTRA)
                    .collect(Collectors.toList());

            boolean hasExtraImage = !extraImages.isEmpty();

            if(hasExtraImage) {
                capturedImages = extraImages;
                currentImageIndex = 0;

                MyImage firstExtraImage = images.get(currentImageIndex);
                showImageForExtra(firstExtraImage);

                lblImageCount.setText((currentImageIndex + 1) + "/" + extraImages.size());
            }
            else {
                lblImageCount.setText("");
            }

            //OrderItem selectedItem = lstItem.getSelectionModel().getSelectedItem();

            //boolean hasExtraImage = imagesByPosition.containsKey(Position.EXTRA);

            btnPrevious.setVisible(hasExtraImage);
            btnNext.setVisible(hasExtraImage);
            lblImageCount.setVisible(hasExtraImage);

            capturedImages = extraImages;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showImageForExtra(MyImage image) {
        imageExtra.getChildren().clear();

        if (image != null) {
            ImageView imageView = new ImageView(new Image(image.toURI()));
            imageView.fitWidthProperty().bind(imageExtra.widthProperty());
            imageView.fitHeightProperty().bind(imageExtra.heightProperty());
            imageView.setPreserveRatio(true);

            imageView.setOnMouseClicked(e -> openImageHandlingScene(image));

            Rectangle overlay = new Rectangle();
            overlay.widthProperty().bind(imageExtra.widthProperty());
            overlay.heightProperty().bind(imageExtra.heightProperty());
            overlay.setFill(Color.color(0, 0, 0, 0));
            overlay.setMouseTransparent(true);

            imageExtra.getChildren().addAll(imageView, overlay);
        }
    }

    private void loadLogList(int orderItemId) throws Exception {
        logItems.clear();

        for(Log log : logModel.getLogsForItem(orderItemId)){

            String itemNumber = lstItem.getItems().stream()
                    .filter(item -> item.getOrderItemId() == log.getOrderItemID())
                    .findFirst()
                    .map(OrderItem::getOrderItem)
                    .orElse("Unknown Item");

            logItems.add(String.format(
                    "%s image %s → %s on item %s by %s",
                    log.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    log.getImagePosition(),
                    log.getAction(),
                    itemNumber,
                    log.getUsername()
            ));
        }
        lstLog.scrollTo(logItems.size() - 1);
    }

    private void appendLog(int orderItemId, String position, String action, String user) throws Exception {
        Log newLog = logModel.addLog(orderItemId, position, action, user);

        logItems.add(formatLogEntry(newLog));
        lstLog.scrollTo(logItems.size() - 1);
    }

    private String formatLogEntry(Log log) {
        String timestamp = log.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        String itemNumber = lstItem.getItems().stream()
                .filter(item -> item.getOrderItemId() == log.getOrderItemID())
                .findFirst()
                .map(OrderItem::getOrderItem)
                .orElse("Unknown Item");
        return String.format("%s image %s → %s on item %s by %s",
                timestamp,
                log.getImagePosition(),
                log.getAction(),
                itemNumber,
                log.getUsername());
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

        btnPrevious.setVisible(false);
        btnNext.setVisible(false);
        lblImageCount.setVisible(false);
    }


    private void setIcons() {

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


        if (index < 0 || index >= capturedImages.size()) return;

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
        try {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/belmanqcreport/FXML/ImageHandling.fxml"));
            Parent root = loader.load();
            ImageHandlingController controller = loader.getController();

            controller.showDeleteButton(false);
            controller.setOrderDetails(order, image, updatedImage -> {

                /*if (updatedImage == null) {
                    imagesByPosition.remove(image.getImagePosition());
                    showImageForPosition(image.getImagePosition());
                    updateImageCountLabel();
                    return;
                }*/


                imagesByPosition.put(updatedImage.getImagePosition(), updatedImage);
                showImageForPosition(updatedImage.getImagePosition());
                updateImageCountLabel();
                OrderItem selected = lstItem.getSelectionModel().getSelectedItem();

                String user = lblEmployee.getText();
                String pos = updatedImage.getImagePosition().name();
                int itemId = lstItem.getSelectionModel().getSelectedItem().getOrderItemId();

                if (updatedImage.getValidationTypeID() == ValidationType.DENIED.getId()) {
                    try {
                        appendLog(itemId, pos, "Denied", user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (updatedImage.getValidationTypeID() == ValidationType.APPROVED.getId()) {
                    try {
                        appendLog(itemId, pos, "Approved", user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }



                Rectangle overlay = imagePanesOverlay.get(updatedImage.getImagePosition());
                if (selected != null) {
                    boolean denied = updatedImage.getValidationTypeID() == ValidationType.DENIED.getId();
                    overlay.setFill(denied
                            ? Color.color(1, 0, 0, 0.3)
                            : Color.color(0, 1, 0, 0.3)
                    );
                }
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
        //lblImageCount.setText(imagesByPosition.size() + " / " + Position.values().length);
        if (!capturedImages.isEmpty() && currentImageIndex >= 0) {
            lblImageCount.setText((currentImageIndex + 1) + " / " + capturedImages.size());
        } else {
            lblImageCount.setText("");
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


    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) throws Exception {
            PDFGeneratorImp.getInstance().setOrder(order);

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


            boolean isDenyAll = idDenyAll.isSelected();
            boolean isApproveAll = idApproveAll.isSelected();

            if (isDenyAll == isApproveAll) {
                showAlert(Alert.AlertType.WARNING, "Please select either Approve or Deny.");
                return;
            }

            String action = isDenyAll ? "Deny" : "Approve";
            OrderItem selectedItem = lstItem.getSelectionModel().getSelectedItem();
            int itemId = selectedItem.getOrderItemId();

            if (!showConfirmation("Confirm " + action, "Are you sure you want to " + action + " item? " + selected.getOrderItem() + "?")) {
                resetCheckBoxes();
                return;
            }

            markAllImages(!isDenyAll);

            resetCheckBoxes();

            for(MyImage img : imagesByPosition.values()) {
                String position = img.getImagePosition().name();
                try{
                    appendLog(itemId, position, action, user);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            if (isDenyAll) {
                updateItemStatus(selected, deniedItems, approvedItems, "Denied", user);
            } else {
                updateItemStatus(selected, approvedItems, deniedItems, "Approved", user);
                savePDF(actionEvent);
            }
        }

        private void updateItemStatus (OrderItem item, Set < OrderItem > addTo, Set < OrderItem > removeFrom, String
        status, String user) throws Exception {
            if (status.equals("Approved")) {

            } else if (status.equalsIgnoreCase("Denied")) {

            }
            addTo.add(item);
            removeFrom.remove(item);
            lstItem.refresh();


            showInfo("Images of item “" + item.getOrderItem() + "” has been " + status.toLowerCase() + ".");

            lstLog.scrollTo(logItems.size() - 1);
            resetCheckBoxes();
            applyOverlayForItem(item, status.equals("Approved"));
        }

        private void applyOverlayForItem (OrderItem selected,boolean approved){
            // clear all overlays
            imagePanesOverlay.values().forEach(r -> r.setFill(Color.color(0, 0, 0, 0)));
            // tint only matching slot(s)
            imagesByPosition.forEach((pos, img) -> {
                if (img.getOrderItemID() == selected.getOrderItemId()) {
                    Rectangle overlay = imagePanesOverlay.get(pos);
                    overlay.setFill(
                            approved
                                    ? Color.color(0, 1, 0, 0.3)   // transparent green
                                    : Color.color(1, 0, 0, 0.3)   // transparent red
                    );
                }
            });
        }

    private void savePDF(ActionEvent actionEvent) {
        try {
            String orderNumber = lstOrder.getSelectionModel().getSelectedItem().getOrderNumber();
            String orderItem = lstItem.getSelectionModel().getSelectedItem().getOrderItem();



            String filename = "Report " + orderNumber + "-" + orderItem + ".pdf";
            File pdfFile = showSaveDialog(filename);
            if (pdfFile != null) {
                PDFGeneratorImp pdfGen = PDFGeneratorImp.getInstance();
                pdfGen.setEmployeeName(lblEmployee.getText());

                List<MyImage> imagesToPdf = new ArrayList<>(imagesByPosition.values());
                pdfGen.generatePDF(pdfFile.getAbsolutePath(), imagesToPdf);

                System.out.println("PDF saved to " + pdfFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Order getSelectedOrder() {
        return orderTableView.getSelectionModel().getSelectedItem();
    }

    private File showSaveDialog (String initialFileName){
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            fileChooser.setInitialFileName(initialFileName);
            return fileChooser.showSaveDialog((Stage) btnPDFSave.getScene().getWindow());
        }

        private boolean showConfirmation (String title, String content){
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.OK, ButtonType.CANCEL);
            confirm.setTitle(title);
            return confirm.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
        }

        private void showAlert (Alert.AlertType type, String message){
            new Alert(type, message).showAndWait();
        }

        private void resetCheckBoxes () {
            idDenyAll.setSelected(false);
            idApproveAll.setSelected(false);
        }




        private void setImageViewIcon (ImageView logoImage, String iconPath){
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




        @FXML
        private void checkApproved (ActionEvent actionEvent){
        }

        @FXML
        private void checkDenied (ActionEvent actionEvent){
        }

        public void setFirstNameAndLastName (String text){
            lblEmployee.setText(text);
        }
    }
