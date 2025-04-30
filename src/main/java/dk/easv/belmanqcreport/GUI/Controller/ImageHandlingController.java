package dk.easv.belmanqcreport.GUI.Controller;
// Other Import
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.GUI.Model.ImageHandlingModel;
import io.github.palexdev.materialfx.controls.MFXButton;
// JavaFX Imports
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.List;

public class ImageHandlingController {

    @FXML
    private Label lblOrderNumber;
    @FXML
    private Label lblEmployee;

    @FXML
    private MFXButton btnBack;
    @FXML
    private MFXButton btnRefresh;
    @FXML
    private MFXButton btnLogout;
    @FXML
    private MFXButton btnSave;
    @FXML
    private MFXTextField txtComment;
    @FXML
    private HBox imageHboxCenter;

    private ImageHandlingModel model;

    @FXML
    private void initialize() throws Exception {
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

    public void setOrderDetails(Order order) {
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
                javafx.scene.layout.BackgroundRepeat.NO_REPEAT,
                javafx.scene.layout.BackgroundRepeat.NO_REPEAT,
                javafx.scene.layout.BackgroundPosition.CENTER,
                new javafx.scene.layout.BackgroundSize(
                        javafx.scene.layout.BackgroundSize.AUTO,
                        javafx.scene.layout.BackgroundSize.AUTO,
                        false, false, true, false
                )
        );
        imageHboxCenter.setBackground(new javafx.scene.layout.Background(backgroundImage));
    }


    @FXML
    private void btnBack(ActionEvent actionEvent) {
    }

    @FXML
    private void btnRefresh(ActionEvent actionEvent) {
    }

    @FXML
    private void btnLogout(ActionEvent actionEvent) {
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
    }





}
