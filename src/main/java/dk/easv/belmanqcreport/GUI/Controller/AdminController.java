package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.User;
// Other Imports
import dk.easv.belmanqcreport.BLL.UTIL.Search;
import dk.easv.belmanqcreport.GUI.Model.UserModel;
import dk.easv.belmanqcreport.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
// JavaFX Imports
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.naming.Context;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class AdminController implements Initializable {


    @FXML
    private Label lblOrderNumber;
    @FXML
    private Label lblEmployee;
    @FXML
    private MFXTextField txtSearch;
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
    public TableColumn colManual;
    @FXML
    public TableColumn colQRCode;
    @FXML
    public TableColumn colChip;
    @FXML
    private MFXButton btnBack;
    @FXML
    private MFXButton btnRefresh;
    @FXML
    private MFXButton btnLogout;

    private UserModel userModel;

    public AdminController() throws IOException {
        userModel = new UserModel();
    }

    private ObservableList<User> users = FXCollections.observableArrayList();
    private List<User> allUsers;
    private Search searchEngine = new Search();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // button icon
        btnBack.setText("");
        setButtonIcon(btnBack, "/dk/easv/belmanqcreport/Icons/backbtn.png");
        btnRefresh.setText("");
        setButtonIcon(btnRefresh, "/dk/easv/belmanqcreport/Icons/refreshbtn.png");
        btnLogout.setText("");
        setButtonIcon(btnLogout, "/dk/easv/belmanqcreport/Icons/logout.png");

        // TabelView
        colId.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colFName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("userType"));

        try {
            tblEmployee.setItems(userModel.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();
        }


        // ListView


        // Search
        users = FXCollections.observableArrayList();
        try{
        userModel = new UserModel();
        allUsers = userModel.getAllUsers();
            System.out.printf("Loaded user:" + allUsers.size());
            for (User u: allUsers){
                System.out.println(u.getFirstName() + "" + u.getLastName());
            }
        users.setAll(allUsers);
        tblEmployee.setItems(users);
        }catch(Exception e){
            e.printStackTrace();
            displayError(e);
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colFName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("userType"));

        System.out.println("txtSearch is null?" + (txtSearch == null));
        txtSearch.setDisable(false);
        txtSearch.setEditable(true);
        txtSearch.requestFocus();


        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            List<User> filtered = searchEngine.search(allUsers, newValue);
            System.out.printf("Search term:" + newValue + "-> Result:" + filtered.size());
            users.setAll(filtered);
        });


        // context menu

        ContextMenu contextMenu = new ContextMenu();

        MenuItem qcRole = new MenuItem("QC");
        MenuItem operatorRole = new MenuItem("Operator");
        MenuItem adminRole = new MenuItem("Admin");
        MenuItem createUser = new MenuItem("Create User");
        MenuItem editUser = new MenuItem("Edit User");
        MenuItem deleteUser = new MenuItem("Delete User");
        MenuItem createManualLogin = new MenuItem("Create manual Login");
        MenuItem createQRLogin = new MenuItem("Create QR Login");
        MenuItem createChipLogin = new MenuItem("Create Chip Login");

        createChipLogin.setDisable(true);
        createQRLogin.setDisable(true);

        Menu assingRole = new Menu("Assign Role");
        Menu loginOptions = new Menu("Login Options");
        Menu usersOptions = new Menu("User Options");

        usersOptions.getItems().addAll(createUser, editUser, deleteUser);
        loginOptions.getItems().addAll(createQRLogin,createManualLogin,createChipLogin);
        assingRole.getItems().addAll(qcRole,operatorRole,adminRole);
        contextMenu.getItems().addAll(assingRole,usersOptions,loginOptions);

        tblEmployee.setContextMenu(contextMenu);
    }

    private void displayError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

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

    private void setButtonIcon(Button button, String iconPath) {
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

}
