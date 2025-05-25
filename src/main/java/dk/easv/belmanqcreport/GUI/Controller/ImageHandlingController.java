package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.DAL.Interface.ValidationType;
import dk.easv.belmanqcreport.GUI.Model.ImageHandlingModel;
import dk.easv.belmanqcreport.GUI.Model.ImageModel;
import dk.easv.belmanqcreport.Main;
// Other Import
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
// JavaFX Imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
// Java Imports
import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

public class ImageHandlingController {

    @FXML
    private Label lblOrderNumber;
    @FXML
    private MFXButton btnDelete;
    @FXML
    private MFXButton btnBackId;
    @FXML
    private MFXButton btnLogoutId;
    @FXML
    private MFXButton btnSaveId;
    @FXML
    private MFXTextField txtComment;
    @FXML
    private HBox imageHboxCenter;
    @FXML
    private
    Label alertLbl;
    @FXML
    private ImageView imageView;

    private OperatorMainController operatorController;

    private ImageHandlingModel model;
    private ImageModel imageModel;

    private Order currentOrder;
    private MyImage currentImage;
    private Consumer<MyImage> onSaveCallBack;

    @FXML
    private ImageView logoImage;
    @FXML
    private MFXCheckbox checkDeny;

    @FXML
    private void initialize() throws Exception {

        setImageViewIcon(logoImage, "/dk/easv/belmanqcreport/Icons/Belman.png");

        btnBackId.setText("");
        setButtonIcon(btnBackId, "/dk/easv/belmanqcreport/Icons/backbtn.png", 20, 20);

        btnLogoutId.setText("");
        setButtonIcon(btnLogoutId, "/dk/easv/belmanqcreport/Icons/logout.png", 20, 20);

        btnSaveId.setText("");
        setButtonIcon(btnSaveId, "/dk/easv/belmanqcreport/Icons/save.png", 50, 50);

        btnDelete.setText("");
        setButtonIcon(btnDelete, "/dk/easv/belmanqcreport/Icons/delete.png", 30, 30);

        this.model = new ImageHandlingModel();
        this.operatorController = new OperatorMainController();
        this.imageModel = new ImageModel();

    }

    public void setOrderDetails(Order order, MyImage image, Consumer<MyImage> onUpdate) {
        this.currentOrder = order;
        this.currentImage = image;
        this.onSaveCallBack = onUpdate;

        lblOrderNumber.setText(String.valueOf(order.getOrderID()));
        txtComment.setText(image.getComment());

        checkDeny.setSelected(image.getValidationTypeID() == ValidationType.DENIED.getId());
        resetCheckbox();
        showImage();
    }

    private void showImage() {
        String path = currentImage.getImagePath();

        System.out.println("Image path: " + path);
        if (path == null || path.isEmpty()) {
            System.err.println("Image path is null or empty");
            return;
        }

        File file = new File(path);
        System.out.println("Image " + file.exists() + " @" + file.getAbsolutePath());
        if (!file.exists()) {
            System.err.println("Image file missing" + path);
            return;
        }
        Image fx = new Image(file.toURI().toString());

        imageView.setImage(fx);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(1400);
        imageView.setFitHeight(600);

    }

    @FXML
    private void btnBack(ActionEvent actionEvent) {
        ((Stage) btnBackId.getScene().getWindow()).close();

    }

    public void showDeleteButton(boolean visible) {
        btnDelete.setVisible(visible);
        btnDelete.setManaged(visible);
    }

    public void showCheckbox(boolean visible) {
        checkDeny.setVisible(visible);
        checkDeny.setManaged(visible);
    }

    @FXML
    private void btnDelete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Image");
        alert.setHeaderText("Are you sure you want to delete this image?");
        alert.setContentText("This action cannot be undone.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            try{
                imageModel.deleteImage(currentImage);

                File file = new File(currentImage.getImagePath());
                if(file.exists()){
                    boolean deleted = file.delete();
                    if(!deleted){
                        System.out.println("Failed to delete image file: " + file.getAbsolutePath());
                    }
                }

                onSaveCallBack.accept(null);

                ((Stage) btnDelete.getScene().getWindow()).close();
            }
            catch(Exception e){
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Failed to delete image");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }
        }
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

    private boolean showConfirmation (String title, String content){
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle(title);
        return confirm.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to save your changes?",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText(null);
        confirm.setTitle("Confirm Save");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        //updates comment
        currentImage.setComment(txtComment.getText());
        try {
            imageModel.updateComment(currentImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //update per-image validation
        int validationTypeID = checkDeny.isSelected()
                ? ValidationType.DENIED.getId()
                : ValidationType.APPROVED.getId();
        try{
            imageModel.updateImageStatus(currentImage.getImageID(), validationTypeID);
            currentImage.setValidationTypeID(validationTypeID);
            if(checkDeny.isSelected()){
            showConfirmation("Image Status Updated", "The image status has been updated to "
                            + (checkDeny.isSelected()
                            ? "Denied" : "Approved") + ".");}
        } catch (Exception e) {
            e.printStackTrace();
        }

        resetCheckbox();
        onSaveCallBack.accept(currentImage);
        ((Stage)btnBackId.getScene().getWindow()).close();
    }

    private void setButtonIcon(MFXButton button, String iconPath, double width, double height) {
        URL iconUrl = getClass().getResource(iconPath);
        if (button == null) {
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
        logoImage.setFitWidth(100);  // Set your desired width
        logoImage.setFitHeight(100); // Set your desired height
        logoImage.setPreserveRatio(true);
    }

    private void resetCheckbox() {
        checkDeny.setSelected(false);
    }


    @FXML
    private void btnScratch(ActionEvent actionEvent) {
        txtComment.setText("minor scratch");
    }

    @FXML
    private void btnDent(ActionEvent actionEvent) {
        txtComment.setText("minor dent");
    }
}
