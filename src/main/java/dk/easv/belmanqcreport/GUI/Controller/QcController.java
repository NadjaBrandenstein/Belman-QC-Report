package dk.easv.belmanqcreport.GUI.Controller;

// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.BLL.CameraHandling;
import dk.easv.belmanqcreport.BLL.UTIL.ImageDataFetcher;
import dk.easv.belmanqcreport.BLL.UTIL.PDFGeneratorImp;
import dk.easv.belmanqcreport.GUI.Model.ImageHandlingModel;
import dk.easv.belmanqcreport.GUI.Model.ImageModel;
import dk.easv.belmanqcreport.Main;
//Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class QcController implements Initializable {

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
    private MFXButton btnPrevious;
    @FXML
    private MFXButton btnNext;
    @FXML
    private MFXButton btnPDFSave;
    @FXML
    private ImageView logoImage;
    @FXML
    private MFXCheckbox checkApproved;
    @FXML
    private MFXCheckbox checkDenied;

    @FXML
    private ListView<Order> lstOrder;
    @FXML
    private ListView<OrderItem> lstItem;

    private ImageHandlingModel imageHandlingModel;
    private ImageModel imageModel;
    private Order order;
    private Window primaryStage;

    private final CameraHandling cameraHandler = new CameraHandling();
    private List<MyImage> capturedImages = new ArrayList<>();
    private int currentImageIndex = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        imageHandlingModel = new ImageHandlingModel();
        try {
            imageModel = new ImageModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        setIcons();

        try {
            populateLists();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        lstOrder.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if(sel != null) {
                try {
                    List<OrderItem> items = imageHandlingModel.getItemsByOrderID(sel.getOrderID());
                    lstItem.getItems().setAll(items);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                lstItem.getItems().clear();
            }
        });

        if(!orders.isEmpty()) {
            lstOrder.getSelectionModel().selectFirst();
        }

        lstItem.getSelectionModel().selectedItemProperty().addListener((obs, old, newItem) -> {
            if(newItem != null){
                try{
                    List<MyImage> images = imageModel.getImageForOrder(order.getOrderID());

                    capturedImages = new ArrayList<>(images);
                    currentImageIndex = images.isEmpty() ? -1 : 0;

                    if(currentImageIndex >= 0){
                        showImageAtIndex(currentImageIndex);
                        updateImageCountLabel();
                    }else {
                        imageHboxCenter.getChildren().clear();
                        lblImageCount.setText("0 / 0");
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

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

        btnPDFSave.setText("");
        setButtonIcon(btnPDFSave, "/dk/easv/belmanqcreport/Icons/pdf.png", 50, 50);

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


        if (index >= 0 && index < capturedImages.size()) {

            MyImage img = capturedImages.get(index);
            Image fxImage = new Image(img.toURI());
            ImageView imageView = new ImageView(fxImage);

            imageView.fitWidthProperty().bind(imageHboxCenter.widthProperty());
            imageView.fitHeightProperty().bind(imageHboxCenter.heightProperty());
            imageHboxCenter.getChildren().clear();

            imageView.setOnMouseClicked(event -> openImageHandlingScene(img));

            imageHboxCenter.getChildren().add(imageView);
        }
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

    public void updateImageCountLabel() {
        lblImageCount.setText((currentImageIndex + 1) + " / " + capturedImages.size());
    }

    public void btnPDFSave(ActionEvent actionEvent) {
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

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        try {
            ImageDataFetcher fetcher = new ImageDataFetcher();


            List<MyImage> allImages = new ArrayList<>();

            allImages.addAll(capturedImages);

            int[] imageIDs = {1, 2, 3};

            for (int imageID : imageIDs) {
                BufferedImage img = fetcher.getImageFromDatabase(imageID); // Or getImageByPathFromDatabase

                if (img != null) {
                    String tempPath = "temp_image_" + imageID + ".png";
                    File tempFile = new File(tempPath);
                    ImageIO.write(img, "png", tempFile);

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
                    MyImage myImage = new MyImage(extractedID, tempPath, comment);
                    myImage.setOrderItemID(orderID);
                    allImages.add(myImage);
                }

            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            fileChooser.setInitialFileName("Report.pdf");
            File pdfFile = fileChooser.showSaveDialog((Stage) ((Node) actionEvent.getSource()).getScene().getWindow());

            if (pdfFile != null) {
                PDFGeneratorImp.getInstance().generatePDF(pdfFile.getAbsolutePath(), allImages);
                System.out.println("PDF saved to " + pdfFile.getAbsolutePath());
            } else {
                System.out.println("Save operation was canceled");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @FXML
    private void checkApproved(ActionEvent actionEvent) {
    }

    @FXML
    private void checkDenied(ActionEvent actionEvent) {
    }

}
