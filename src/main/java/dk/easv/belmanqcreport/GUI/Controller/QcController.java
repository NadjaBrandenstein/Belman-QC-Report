package dk.easv.belmanqcreport.GUI.Controller;

// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BLL.CameraHandling;
import dk.easv.belmanqcreport.Main;
//Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
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
import java.util.ArrayList;
import java.util.List;

public class QcController {

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

    private final CameraHandling cameraHandler = new CameraHandling();
    private final List<MyImage> capturedImages = new ArrayList<>();
    private int currentImageIndex = -1;

    private Window primaryStage;

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
            stage.setTitle("Belman");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnPrevious(ActionEvent actionEvent) {
        if(currentImageIndex > 0) {
            currentImageIndex--;
            showImageAtIndex(currentImageIndex);
            updateImageCountLabel();
        }
    }

    public void btnNext(ActionEvent actionEvent) {
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
            controller.setQcController(this);

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
}
