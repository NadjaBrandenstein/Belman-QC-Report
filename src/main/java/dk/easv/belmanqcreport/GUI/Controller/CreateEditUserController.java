package dk.easv.belmanqcreport.GUI.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckListView;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateEditUserController implements Initializable {

    public MFXTextField txtFirstName;
    public MFXTextField txtLastName;
    public MFXButton btnSave;
    public MFXButton btnCancel;
    public MFXCheckListView lstRoles;
    public MFXRadioButton radioManual;

    public CreateEditUserController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // set button icon

        btnSave.setText("");
        setButtonIcon(btnSave, "/dk/easv/belmanqcreport/Icons/save.png");
        btnCancel.setText("");
        setButtonIcon(btnCancel, "/dk/easv/belmanqcreport/Icons/delete.png");
    }

    public void btnSave(ActionEvent actionEvent) {

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

}
