package dk.easv.belmanqcreport.GUI.Controller;
// Other Imports
import dk.easv.belmanqcreport.BE.Login;
import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.BLL.UTIL.AuthService;
import dk.easv.belmanqcreport.BLL.UTIL.FXMLNavigator;
// Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
// JavaFX Imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
// Java Imports
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public MFXTextField txtUsername;
    public MFXPasswordField txtPassword;
    public ImageView logoImage;
    public Label lblForgotPassword;
    public Label lblLoginStatus;
    public MFXButton btnLogin;
    public MFXButton btnBack;
    private Stage stage;
    private Login login;
    private User user;

    private AuthService authService;
    @FXML
    private HBox imageHboxCenter;


    public LoginController() throws IOException {
        authService = new AuthService();
        login = new Login();
        user = new User();
        authService.setLoginController(this);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setImageViewIcon(logoImage, "/dk/easv/belmanqcreport/Icons/Belman.png");

        btnBack.setText("");
        setButtonIcon(btnBack, "/dk/easv/belmanqcreport/Icons/backbtn.png");



        lblForgotPassword.setStyle("-fx-text-fill: blue; -fx-underline: true;"); // Make it look like a link
        lblForgotPassword.setCursor(Cursor.HAND); // Change cursor to hand on hover

        lblForgotPassword.setOnMouseClicked(event -> {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLNavigator.getInstance().navigateTo(currentStage, "dk/easv/belmanqcreport/FXML/ForgotPassword.fxml");

        });
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
        FXMLLoader loader = FXMLNavigator.getInstance().navigateTo(stage, "dk/easv/belmanqcreport/FXML/Admin.fxml");

        if(loader != null) {
            AdminController adminController = loader.getController();
            adminController.setUserName(this.txtUsername.getText());
            adminController.setStage(this.stage);
        }
    }

    private void opratorPage() {
        FXMLLoader loader = FXMLNavigator.getInstance().navigateTo(stage, "dk/easv/belmanqcreport/FXML/OperatorSearch.fxml");

        if (loader != null) {
            OperatorSearchController controller = loader.getController();
            controller.setUserName(this.txtUsername.getText());
        }
    }

    private void qcPage() throws IOException {
        FXMLLoader loader = FXMLNavigator.getInstance().navigateTo(stage, "dk/easv/belmanqcreport/FXML/QC.fxml");

        if (loader != null) {
            QcController qcController = loader.getController();
            qcController.setUserName(this.txtUsername.getText());
        }
    }

    public void btnLoginKey(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            btnLogin.fire(); // This doesn't work unless you reference the button directly
        }
    }

    private void setButtonIcon(MFXButton button, String iconPath) {
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

    private void setImageViewIcon(ImageView logoImage, String iconPath) {
        if (logoImage == null) {
            System.out.println("logoImage is null. Cannot set icon: " + iconPath);
            return;
        }

        URL iconUrl = getClass().getResource(iconPath);
        if (iconUrl == null) {
            System.out.println("Error loading icon: " + iconPath);
            return;
        }

        Image icon = new Image(iconUrl.toExternalForm());
        logoImage.setImage(icon);
        logoImage.setFitWidth(100);  // Set your desired width
        logoImage.setFitHeight(100); // Set your desired height
        logoImage.setPreserveRatio(true);
    }

}
