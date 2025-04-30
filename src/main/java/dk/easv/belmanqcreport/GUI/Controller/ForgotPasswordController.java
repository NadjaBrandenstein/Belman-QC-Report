package dk.easv.belmanqcreport.GUI.Controller;

import dk.easv.belmanqcreport.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordController {
    @FXML
    public ImageView logoImage;
    @FXML
    public MFXTextField txtUsername;
    @FXML
    public MFXPasswordField txtNewPassword;
    @FXML
    public MFXPasswordField txtConfirmPassword;
    @FXML
    public MFXButton btnChangePassword;
    @FXML
    public Label lblLoginStatus;

    @FXML
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


    @FXML
    public void btnChangePassword(ActionEvent keyEvent) {
    }

    @FXML
    public void btnChangePasswordKey(KeyEvent keyEvent) {
    }
}
