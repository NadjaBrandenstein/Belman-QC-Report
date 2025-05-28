package dk.easv.belmanqcreport.GUI.Controller;
// Project Imports
import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.GUI.Model.UserModel;
// Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
// JavaFX Imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
// Java Imports
import java.net.URL;
import java.util.ResourceBundle;

public class CreateEditUserController implements Initializable {

    public MFXTextField txtFirstName;
    public MFXTextField txtLastName;
    public MFXButton btnSave;
    public MFXButton btnCancel;

    public MFXRadioButton radioManual;
    public ListView<String> lstRoleCheckList;

    private UserModel userModel;
    private Stage stage;

    private boolean isEditMode = false;
    private User editingUser;

    public CreateEditUserController() throws Exception {
        userModel = new UserModel();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // set button icon
        btnSave.setText("");
        setButtonIcon(btnSave, "/dk/easv/belmanqcreport/Icons/save.png");
        btnCancel.setText("");
        setButtonIcon(btnCancel, "/dk/easv/belmanqcreport/Icons/delete.png");

        ObservableList<String> lstRoles = FXCollections.observableArrayList("qc", "operator", "admin");
        lstRoleCheckList.getItems().setAll(lstRoles);
    }

    public void btnSave(ActionEvent actionEvent) throws Exception {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText(null);
            alert.setContentText("First name and last name are required.");
            alert.showAndWait();
            return;
        }

        // Get the selected role(s)
        lstRoleCheckList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Get selected role
        String selectedRole = lstRoleCheckList.getSelectionModel().getSelectedItem();

        if (selectedRole == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Role Selected");
            alert.setHeaderText(null);
            alert.setContentText("At least one role must be selected.");
            alert.showAndWait();
            return;
        }

        // Map the selected role to the corresponding ID
        int userTypeID = 0;
        switch (selectedRole.toLowerCase()) {
            case "qc":
                userTypeID = 1; // qc -> 1
                break;
            case "operator":
                userTypeID = 2; // operator -> 2
                break;
            case "admin":
                userTypeID = 3; // admin -> 3
                break;
            default:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Role");
                alert.setHeaderText(null);
                alert.setContentText("The selected role is not valid.");
                alert.showAndWait();
                return;
        }

        // Create new user
        User newUser = new User(firstName, lastName);
        newUser.setUserTypeID(userTypeID); // Set the role ID for the new user

        if (isEditMode && editingUser != null) {
            editingUser.setFirstName(firstName);
            editingUser.setLastName(lastName);
            editingUser.setUserTypeID(userTypeID); // Update the role ID
            userModel.updateUser(editingUser);
        } else {
            userModel.createUser(newUser);
        }

        // Close the stage after saving
        if (stage != null) stage.close();
    }

    public void btnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
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

    public void setUserForEdit(User user) {
        if (user != null) {
            isEditMode = true;
            editingUser = user;
            txtFirstName.setText(user.getFirstName());
            txtLastName.setText(user.getLastName());
            // You can also set roles/login type if needed
        }
    }

}
