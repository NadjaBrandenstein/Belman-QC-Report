package dk.easv.belmanqcreport.GUI.Controller;
// Other Imports
import dk.easv.belmanqcreport.BE.Login;
import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.BLL.Manager.LoginManager;
import dk.easv.belmanqcreport.BLL.UTIL.AuthService;
import dk.easv.belmanqcreport.Main;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
// JavaFX Imports
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public MFXTextField txtUsername;
    public MFXPasswordField txtPassword;
    public ImageView logoImage;
    public Label lblForgotPassword;
    public Label lblLoginStatus;
    private Stage stage;
    private Login login;
    private User user;

    private AuthService authService;
    public LoginController() throws IOException {
        authService = new AuthService();
        login = new Login();
        user = new User();
    }


    public void btnBack(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }


    public void btnLogout(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void btnQRCode(ActionEvent actionEvent) {

    }

    public void btnManual(ActionEvent actionEvent) {

    }

    public void btnChip(ActionEvent actionEvent) {

    }

    public void btnLogin(ActionEvent actionEvent) throws IOException {
        String username = this.txtUsername.getText();
        String password = this.txtPassword.getText();

        Login login = authService.login(username, password);

        if (login != null) {
            lblLoginStatus.setText("Login successful!");
            String userType = login.getUserType();

            if (userType != null) {
                // Get the current stage from the event and assign it
                this.stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                switch (userType.toLowerCase()) {
                    case "admin":
                        adminpage();
                        break;
                    case "operator":
                        opratorPage();
                        break;
                    case "qc":
                        qcPage();
                        break;
                    default:
                        lblLoginStatus.setText("Unknown role: " + userType);
                }
            } else {
                lblLoginStatus.setText("User type is null.");
            }
        } else {
            lblLoginStatus.setText("Login failed.");
        }
    }



    private void adminpage() throws IOException {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dk/easv/belmanqcreport/FXML/Admin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());
        stage.setTitle("Belman");
        stage.setScene(scene);
        stage.show();
    }

    private void opratorPage() throws IOException {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dk/easv/belmanqcreport/FXML/Operator.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());
        stage.setTitle("Belman");
        stage.setScene(scene);
        stage.show();
    }

    private void qcPage() throws IOException {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dk/easv/belmanqcreport/FXML/QC.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());
        stage.setTitle("Belman");
        stage.setScene(scene);
        stage.show();
    }
}
