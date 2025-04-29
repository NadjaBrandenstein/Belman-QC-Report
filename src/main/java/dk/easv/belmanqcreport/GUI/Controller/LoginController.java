package dk.easv.belmanqcreport.GUI.Controller;
// Other Imports
import dk.easv.belmanqcreport.BLL.UTIL.AuthService;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
// JavaFX Imports
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class LoginController {
    public MFXTextField txtUsername;
    public MFXPasswordField txtPassword;
    public ImageView logoImage;
    public Label lblForgotPassword;
    public Label lblLoginStatus;

    private AuthService authService;
    public LoginController() throws IOException {
        authService = new AuthService();
    }


    public void btnBack(ActionEvent actionEvent) {

    }


    public void btnLogout(ActionEvent actionEvent) {

    }

    public void btnQRCode(ActionEvent actionEvent) {

    }

    public void btnManual(ActionEvent actionEvent) {

    }

    public void btnChip(ActionEvent actionEvent) {

    }

    public void btnLogin(ActionEvent actionEvent) {
        String username = this.txtUsername.getText();
        String password = this.txtPassword.getText();

        boolean success = authService.login(username, password);

        if (success) {
            lblLoginStatus.setText("Login successful!");
            // proceed to next scene
        } else {
            lblLoginStatus.setText("Login failed.");
        }
    }
}
