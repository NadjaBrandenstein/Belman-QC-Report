package dk.easv.belmanqcreport.GUI.Controller;

// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BLL.CameraHandling;
import dk.easv.belmanqcreport.GUI.Model.ImageHandlingModel;
import dk.easv.belmanqcreport.Main;
//Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.util.StringConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
// JavaFX Imports
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.Window;
// Java Imports
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class QcController implements Initializable {

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
    private MFXButton btnPDFSave;
    @FXML
    private MFXComboBox<Order> cbOrderNumber;

    private ImageHandlingModel imageHandlingModel;
    private Order currentOrder;

    private final CameraHandling cameraHandler = new CameraHandling();
    private List<MyImage> capturedImages = new ArrayList<>();
    private int currentImageIndex = -1;

    private Window primaryStage;
    @FXML
    private ImageView logoImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImageViewIcon(logoImage, "/dk/easv/belmanqcreport/Icons/Belman.png");
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
        btnPDFSave.setText("");
        setButtonIcon(btnPDFSave, "/dk/easv/belmanqcreport/Icons/save.png", 50, 50);

        imageHandlingModel = new ImageHandlingModel();

        try {
            List<Order> orders = imageHandlingModel.getAllOrders();
            cbOrderNumber.getItems().addAll(orders);

            cbOrderNumber.setConverter(new StringConverter<>() {
                @Override
                public String toString(Order order) {
                    return order == null ? "" : String.valueOf(order.getOrderNumber());
                }

                @Override
                public Order fromString(String string) {
                    return null;
                }
            });

            cbOrderNumber.setOnAction(event -> {
                currentOrder = cbOrderNumber.getSelectedItem();
                capturedImages = new ArrayList<>(currentOrder.getImages());
                displayImages(capturedImages);
            });

            if (!orders.isEmpty()) {
                cbOrderNumber.getSelectionModel().selectFirst();
                currentOrder = cbOrderNumber.getSelectedItem();
                capturedImages = new ArrayList<>(currentOrder.getImages());
                displayImages(capturedImages);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


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

    public void btnRefresh(ActionEvent actionEvent) {
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

    @FXML
    private void btnCamera(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/belmanqcreport/FXML/Camera.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Live Camera Preview");
            stage.setScene(scene);

            stage.setResizable(true);
            stage.setMaximized(true);

            CameraController controller = loader.getController();
            controller.setQcController(this);

            stage.setOnCloseRequest(event -> controller.cleanup());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open camera.");
            alert.showAndWait();
        }
    }

    public void displayCapturedImage(MyImage myImg) {
        Platform.runLater(() -> {

            capturedImages.add(myImg);
            currentImageIndex = capturedImages.size() - 1;
            showImageAtIndex(currentImageIndex);
            updateImageCountLabel();

        });
    }

    public void showImageAtIndex(int index) {
        if (index >= 0 && index < capturedImages.size()) {
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

    @FXML
    private void cbOrderNumber(ActionEvent actionEvent) {

        Order selectedOrder = cbOrderNumber.getSelectedItem();
        if (selectedOrder != null) {
            currentOrder = selectedOrder;
            capturedImages = new ArrayList<>(currentOrder.getImages());
            currentImageIndex = 0;
            if (!capturedImages.isEmpty()) {
                showImageAtIndex(currentImageIndex);
                updateImageCountLabel();
            } else {
                imageHboxCenter.getChildren().clear();
                lblImageCount.setText("0 / 0");
            }
        }

    }

    private void displayImages(List<MyImage> capturedImages) {
        imageHboxCenter.getChildren().clear();
        for (MyImage image : capturedImages) {
            ImageView imageView = new ImageView(String.valueOf(image));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            imageHboxCenter.getChildren().add(imageView);
        }
    }
}
