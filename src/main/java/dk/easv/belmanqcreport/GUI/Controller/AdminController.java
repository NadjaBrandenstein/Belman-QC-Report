package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.User;
// Other Imports
import dk.easv.belmanqcreport.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
// JavaFX Imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;


public class AdminController {

    @FXML
    private Label lblOrderNumber;
    @FXML
    private Label lblEmployee;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<User> tblEmployee;
    @FXML
    private TableColumn<User, String> colId;
    @FXML
    private TableColumn<User, String> colFName;
    @FXML
    private TableColumn<User, String> colLName;
    @FXML
    private TableColumn<User, String> colRole;
    @FXML
    private ListView<Order> lstOrderNumber;

    @FXML
    private MFXButton btnBack;
    @FXML
    private MFXButton btnRefresh;
    @FXML
    private MFXButton btnLogout;


    @FXML
    private void btnBack(ActionEvent actionEvent) {
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
    private void txtSearch(ActionEvent actionEvent) {
        
    }
}
