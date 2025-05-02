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
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.naming.Context;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
    private MFXButton btnBack;
    @FXML
    private MFXButton btnLogout;

    private UserModel userModel;
    private CreateEditUserController createEditUserController;

    public AdminController() throws IOException {
        userModel = new UserModel();
        createEditUserController = new CreateEditUserController();
    }

    private ObservableList<User> users = FXCollections.observableArrayList();
    private List<User> allUsers;
    private Search searchEngine = new Search();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // button icon
        btnBack.setText("");
        setButtonIcon(btnBack, "/dk/easv/belmanqcreport/Icons/backbtn.png");
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

        // action on the context menu
        deleteUser.setOnAction((ActionEvent event) -> {
            User SelectedUser = tblEmployee.getSelectionModel().getSelectedItem();
            if (SelectedUser != null) {
                try {
                    deleteUser();
                } catch (Exception e) {
                    displayError(e);
                }
            }
        });

        createUser.setOnAction((ActionEvent event) -> {
            User SelectedUser = tblEmployee.getSelectionModel().getSelectedItem();
            if (SelectedUser != null) {
                try {
                    createUser();
                } catch (Exception e) {
                    displayError(e);
                }
            }
        });

        editUser.setOnAction((ActionEvent event) -> {
            User SelectedUser = tblEmployee.getSelectionModel().getSelectedItem();
            if (SelectedUser != null) {
                try {
                    editUser();
                } catch (Exception e) {
                    displayError(e);
                }
            }
        });

        qcRole.setOnAction((ActionEvent event) -> {
            User SelectedUser = tblEmployee.getSelectionModel().getSelectedItem();
            if (SelectedUser != null) {
                try {

                } catch (Exception e) {
                    displayError(e);
                }
            }
        });

        operatorRole.setOnAction((ActionEvent event) -> {
            User SelectedUser = tblEmployee.getSelectionModel().getSelectedItem();
            if (SelectedUser != null) {
                try {

                } catch (Exception e) {
                    displayError(e);
                }
            }
        });

        adminRole.setOnAction((ActionEvent event) -> {
            User SelectedUser = tblEmployee.getSelectionModel().getSelectedItem();
            if (SelectedUser != null) {
                try {

                } catch (Exception e) {
                    displayError(e);
                }
            }
        });

        createManualLogin.setOnAction((ActionEvent event) -> {
            User SelectedUser = tblEmployee.getSelectionModel().getSelectedItem();
            if (SelectedUser != null) {
                try {

                } catch (Exception e) {
                    displayError(e);
                }
            }
        });

        createQRLogin.setOnAction((ActionEvent event) -> {
            User SelectedUser = tblEmployee.getSelectionModel().getSelectedItem();
            if (SelectedUser != null) {
                try {

                } catch (Exception e) {
                    displayError(e);
                }
            }
        });

        createChipLogin.setOnAction((ActionEvent event) -> {
            User SelectedUser = tblEmployee.getSelectionModel().getSelectedItem();
            if (SelectedUser != null) {
                try {

                } catch (Exception e) {
                    displayError(e);
                }
            }
        });
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

    private void setRole(){

    }

    private void createUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dk/easv/belmanqcreport/FXML/CreateEditUser.fxml"));

        // Load FXML and get the controller
        Scene scene = new Scene(fxmlLoader.load());

        // Open the Add/Edit Event stage
        Stage stage = new Stage();
        //stage.getIcons().add(new Image("/dk.easv/eventticketeasvbar/Icon/Skærmbillede 2025-03-27 142743.png"));
        stage.setTitle("Create");
        stage.setScene(scene);
        //reference to cancel button
        createEditUserController = fxmlLoader.getController();
        // Make the new stage modal, blocking interaction with the previous window
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private void editUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dk/easv/belmanqcreport/FXML/CreateEditUser.fxml"));

        // Load FXML and get the controller
        Scene scene = new Scene(fxmlLoader.load());

        // Open the Add/Edit Event stage
        Stage stage = new Stage();
        //stage.getIcons().add(new Image("/dk.easv/eventticketeasvbar/Icon/Skærmbillede 2025-03-27 142743.png"));
        stage.setTitle("Create");
        stage.setScene(scene);
        //reference to cancel button
        createEditUserController = fxmlLoader.getController();
        // Make the new stage modal, blocking interaction with the previous window
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private void deleteUser() throws Exception {
        User selectedUser = tblEmployee.getSelectionModel().getSelectedItem();

        if(selectedUser != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirmation");
            alert.setContentText("Are you sure you want to delete this user?");

            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if(result == ButtonType.OK) {
                userModel.deleteUser(selectedUser);
                tblEmployee.getItems().remove(selectedUser);
            }
        }
    }
}
