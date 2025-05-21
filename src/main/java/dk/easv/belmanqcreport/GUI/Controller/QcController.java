package dk.easv.belmanqcreport.GUI.Controller;

// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.BLL.UTIL.CameraHandling;
import dk.easv.belmanqcreport.BLL.UTIL.ImageDataFetcher;
import dk.easv.belmanqcreport.BLL.UTIL.PDFGenerator;
import dk.easv.belmanqcreport.BLL.UTIL.PDFGeneratorImp;
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
import javafx.scene.layout.AnchorPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
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

public class QcController implements Initializable {

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
                try{
                    List<MyImage> imgs = imageModel.getImageForOrder(newItem.getOrderItemId());

                    System.out.println("DEBUG: loadImagesForItem(" + newItem.getOrderItemId() + ") → " + imgs.size() + " images");

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

    private void clearImages() {
        imageFront.getChildren().clear();
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

    /*public void btnPDFSave(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        fileChooser.setInitialFileName("Report.pdf");

        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);


                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);


                contentStream.newLineAtOffset(100, 750);

                contentStream.showText("This is a sample PDF report.");
                contentStream.endText();
                contentStream.close();

                document.save(file);
                System.out.println("PDF saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }*/

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



   /* @FXML
    private void btnSave(ActionEvent actionEvent) {

        OrderItem selected = lstItem.getSelectionModel().getSelectedItem();
        String user = lblEmployee.getText();

        if (selected == null) {

            new Alert(Alert.AlertType.WARNING, "No order item selected.").showAndWait();
            return;
        }

        boolean isDeny = idDenied.isSelected();
        boolean isApprove = idApproved.isSelected();

        if ((isDeny && isApprove) || (!isDeny && !isApprove)) {
            new Alert(Alert.AlertType.WARNING, "Please select either Approve or Deny.").showAndWait();
            return;
        }

        String verb = isDeny ? "Deny" : "Approve";

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm " + verb);
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to " + verb + " item? " + selected.getOrderItem() + "?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {

            idDenied.setSelected(false);
            idApproved.setSelected(false);
            return;
        }

        if(isDeny) {
            deniedItems.add(selected);
            approvedItems.remove(selected);
            lstItem.refresh();

            showInfo("Item “" + selected.getOrderItem() + "” has been denied.");

            logItems.add(String.format("%s item %s by %s", "Denied", selected.getOrderItem(), user));
            lstLog.scrollTo(logItems.size() - 1);

            idDenied.setSelected(false);
            return;

        } if(isApprove) {
            approvedItems.add(selected);
            deniedItems.remove(selected);
            lstItem.refresh();

            showInfo("Item “" + selected.getOrderItem() + "” has been approved.");

            idApproved.setSelected(false);
            logItems.add(String.format("%s item %s by %s", "Approved", selected.getOrderItem(), user));
            lstLog.scrollTo(logItems.size() - 1);


        Order myOrder = new Order();
        myOrder.setOrderNumber(order.getOrderNumber());
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItem("Item - 001");
        PDFGeneratorImp generator = PDFGeneratorImp.getInstance();

        generator.setOrder(myOrder);
        generator.setOrderItem(orderItem);
        generator.setEmployeeName("");

        List<MyImage> myImages = getImageList();

        generator.generatePDF("QC_report.pdf", myImages);
        try {
            ImageDataFetcher fetcher = new ImageDataFetcher();


            List<MyImage> allImages = new ArrayList<>();

            allImages.addAll(capturedImages);

            int[] imageIDs = {1, 2, 3};

            for (int imageID : imageIDs) {
                BufferedImage img = fetcher.getImageFromDatabase(imageID); // Or getImageByPathFromDatabase

                if (img != null) {
                    String tempPath = new File(System.getProperty("java.io.tmpdir"), "temp_" + imageID + ".png").getAbsolutePath();
                    File tempFile = new File(tempPath);
                    ImageIO.write(img, "png", tempFile);
                    if (!tempFile.exists()) {
                        System.out.println("Failed to create temp image file:" + tempFile.getAbsolutePath());
                    }

                    String comment = fetcher.getCommentByImageID(imageID);
                    int orderID = fetcher.getOrderIDByImageID(imageID);


                    String fileName = tempFile.getName();
                    String[] parts = fileName.split("_");
                    int extractedID = -1;
                    try{
                        extractedID = Integer.parseInt(parts[parts.length-1].replace(".png", ""));
                    }catch (NumberFormatException e){
                        System.out.println("Failed to extract image ID from file name: " + fileName);
                    }
                    MyImage myImage = new MyImage(extractedID, tempPath, comment, imagePositionID);
                    myImage.setOrderItemID(orderID);
                    allImages.add(myImage);
                }

            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            fileChooser.setInitialFileName("Report.pdf");
            File pdfFile = fileChooser.showSaveDialog((Stage) ((Node) actionEvent.getSource()).getScene().getWindow());

            if (pdfFile != null) {
                PDFGeneratorImp pdfGen = PDFGeneratorImp.getInstance();
                pdfGen.setEmployeeName(lblEmployee.getText());
                pdfGen.generatePDF(pdfFile.getAbsolutePath(), capturedImages);
                PDFGeneratorImp.getInstance().generatePDF(pdfFile.getAbsolutePath(), allImages);
                System.out.println("PDF saved to " + pdfFile.getAbsolutePath());

                PDFGeneratorImp pdfGen2 = PDFGeneratorImp.getInstance();
                pdfGen2.setOrder(order);
                OrderItem orderItem2 = new OrderItem();
                pdfGen2.setOrderItem(orderItem2);
                pdfGen2.setEmployeeName("");
                List<MyImage> imageList = List.of();
                pdfGen2.generatePDF("your-file.pdf", imageList);

            } else {
                System.out.println("Save operation was canceled");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

        ;
    }*/

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
            item.setOrderItem(item.getOrderItem() + " (Approved)");
        }else if(status.equalsIgnoreCase("Denied")){
            item.setOrderItem(item.getOrderItem() + " (Denied)");
        }
        addTo.add(item);
        removeFrom.remove(item);
        item.setOrderItem("NewOrderItem: " + item.getOrderItem());
        lstItem.refresh();
        showInfo("Item “" + item.getOrderItem() + "” has been " + status.toLowerCase() + ".");
        logItems.add(String.format("%s item %s by %s", status, item.getOrderItem(), user));
        lstLog.scrollTo(logItems.size() - 1);
        resetCheckBoxes();
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
