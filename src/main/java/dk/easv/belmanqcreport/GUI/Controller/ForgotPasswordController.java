package dk.easv.belmanqcreport.GUI.Controller;
//Project Imports
import dk.easv.belmanqcreport.BE.Login;
import dk.easv.belmanqcreport.DAL.Database.LoginRepository;
import dk.easv.belmanqcreport.Main;
// Other Imports
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
// JavaFX Imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
// Java Imports
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ForgotPasswordController implements Initializable {
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
    private MFXButton btnBack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setImageViewIcon(logoImage, "/dk/easv/belmanqcreport/Icons/Belman.png");

        btnBack.setText("");
        setButtonIcon(btnBack, "/dk/easv/belmanqcreport/Icons/backbtn.png");
    }

    @FXML
    public void btnBack(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dk/easv/belmanqcreport/FXML/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());
            stage.getIcons().add(new Image("/dk/easv/belmanqcreport/Icons/Belman.png"));
            stage.setTitle("Belman");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnChangePassword(ActionEvent keyEvent) {
        String username = txtUsername.getText();
        String newPassword = txtNewPassword.getText().trim();
        String confirmPassword = txtConfirmPassword.getText().trim();

        if (username.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            lblLoginStatus.setText("Please fill in all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            lblLoginStatus.setText("Passwords do not match.");
            return;
        }

        try {
            LoginRepository loginRepository = new LoginRepository();
            Login user = loginRepository.getLoginByUsername(username);

            if (user == null) {
                lblLoginStatus.setText("User not found.");
                return;
            }
            user.setPassword(newPassword);

            loginRepository.update(user);

            lblLoginStatus.setText("Password changed successfully.");
        }catch (Exception e){
            lblLoginStatus.setText("Error changing password.");
            e.printStackTrace();
        }
    }

    @FXML
    public void btnChangePasswordKey(KeyEvent keyEvent) {
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
