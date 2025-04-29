package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.User;
// Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
// JavaFX Imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


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
    }

    @FXML
    private void btnRefresh(ActionEvent actionEvent) {
    }

    @FXML
    private void btnLogout(ActionEvent actionEvent) {
    }

    @FXML
    private void txtSearch(ActionEvent actionEvent) {
        
    }
}
