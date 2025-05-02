package dk.easv.belmanqcreport.GUI.Controller;
// Other Import
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.GUI.Model.ImageHandlingModel;
import dk.easv.belmanqcreport.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
// JavaFX Imports
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.*;

import java.io.File;
import java.net.URL;
import java.util.List;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.BackgroundPosition.CENTER;
import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;
import static javafx.scene.layout.BackgroundSize.AUTO;

public class ImageHandlingController {

    @FXML
    private Label lblOrderNumber;
    @FXML
    private Label lblEmployee;

    @FXML
    private MFXButton btnBackId;
    @FXML
    private MFXButton btnRefreshId;
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

    private ImageHandlingModel model;
    private Order currentOrder;
    private MyImage currentImage;
    @FXML
    private ImageView logoImage;

    @FXML
    private void initialize() throws Exception {

        setImageViewIcon(logoImage, "/dk/easv/belmanqcreport/Icons/Belman.png");

        btnBackId.setText("");
        setButtonIcon(btnBackId, "/dk/easv/belmanqcreport/Icons/backbtn.png");

        btnRefreshId.setText("");
        setButtonIcon(btnRefreshId, "/dk/easv/belmanqcreport/Icons/refreshbtn.png");

        btnLogoutId.setText("");
        setButtonIcon(btnLogoutId, "/dk/easv/belmanqcreport/Icons/logout.png");

        btnSaveId.setText("");
        setButtonIcon(btnSaveId, "/dk/easv/belmanqcreport/Icons/save.png");

       this.model = new ImageHandlingModel();

       try {
           List<Order> orders = model.getAllOrders();
           if (!orders.isEmpty()) {
               setOrderDetails(orders.get(0));

           }
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    public void setImageDetails(MyImage img){
        this.currentImage = img;

        Image fx = new Image(new File(img.toURI()).toURI().toString());

        imageHboxCenter.setBackground(new Background(
                new BackgroundImage(
                        fx, NO_REPEAT, NO_REPEAT, CENTER,
                        new BackgroundSize(AUTO, AUTO, false, false, true, false)
                )
        ));
        txtComment.setText(img.getComment());
    }

    public void setOrderDetails(Order order) {
        this.currentOrder = order;
        lblOrderNumber.setText(String.valueOf(order.getOrderID()));
        txtComment.setText(order.getComment());

        String imagePath = order.getImagePath();
        if (imagePath == null || imagePath.isEmpty()) {
            System.err.println("Image path is null or empty");
            return;
        }

        File file = new File(imagePath);
        if (!file.exists()) {
            System.err.println("Image file missing" + imagePath);
            return;
        }
        Image image = new Image(file.toURI().toString());
        javafx.scene.layout.BackgroundImage backgroundImage = new javafx.scene.layout.BackgroundImage(
                image,
                NO_REPEAT,
                NO_REPEAT,
                CENTER,
                new javafx.scene.layout.BackgroundSize(
                        AUTO,
                        AUTO,
                        false, false, true, false
                )
        );
        imageHboxCenter.setBackground(new javafx.scene.layout.Background(backgroundImage));
    }


    @FXML
    private void btnBack(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dk/easv/belmanqcreport/FXML/Operator.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());
            stage.getIcons().add(new Image("/dk/easv/belmanqcreport/Icons/Belman.png"));
            stage.setTitle("Belman");
            stage.setScene(scene);
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
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        String newComment = txtComment.getText();
        currentOrder.setComment(newComment);
        try {
            model.updateOrder(currentOrder);
            alertLbl.setText("Saved!");
        } catch (Exception e) {
            e.printStackTrace();
            alertLbl.setText("Failed!");
        }
    }

    private void setButtonIcon(MFXButton button, String iconPath) {
        URL iconUrl = getClass().getResource(iconPath);
        if (button == null) {
            System.out.println("Error loading icon: " + iconPath);
            return;
        }

        Image icon = new Image(iconUrl.toExternalForm());
        ImageView imageView = new ImageView(icon);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
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


}
