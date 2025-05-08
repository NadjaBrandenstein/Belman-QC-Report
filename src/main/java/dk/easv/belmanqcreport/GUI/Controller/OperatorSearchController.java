package dk.easv.belmanqcreport.GUI.Controller;

import dk.easv.belmanqcreport.BLL.UTIL.FXMLNavigator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class OperatorSearchController implements Initializable {
    @FXML
    private ImageView logoImage;
    @FXML
    private MFXButton btnBack;
    @FXML
    private Label lblEmployee;
    @FXML
    private MFXButton btnLogout;
    @FXML
    private HBox imageHboxCenter;
    @FXML
    private MFXTextField txtSerach;
    @FXML
    private MFXButton btnSearch;

    private Stage stage;

    public OperatorSearchController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Button icon
        setImageViewIcon(logoImage, "/dk/easv/belmanqcreport/Icons/Belman.png");
        btnBack.setText("");
        setButtonIcon(btnBack, "/dk/easv/belmanqcreport/Icons/backbtn.png");
        btnLogout.setText("");
        setButtonIcon(btnLogout, "/dk/easv/belmanqcreport/Icons/logout.png");


    }

    public void btnLogout(ActionEvent actionEvent) {
        if (stage == null) {
            stage = (Stage) btnBack.getScene().getWindow();
        }
        FXMLNavigator.navigateTo(stage, "dk/easv/belmanqcreport/FXML/Login.fxml");
    }

    public void btnBack(ActionEvent actionEvent) {
        if (stage == null) {
            stage = (Stage) btnBack.getScene().getWindow();
        }
        FXMLNavigator.navigateTo(stage, "dk/easv/belmanqcreport/FXML/Login.fxml");
    }

    public void btnSearch(ActionEvent actionEvent) {
        if (stage == null) {
            // If stage is null, we initialize it using the current button's scene
            stage = (Stage) btnSearch.getScene().getWindow();
        }
        FXMLLoader loader = FXMLNavigator.navigateTo(stage, "dk/easv/belmanqcreport/FXML/OperatorMain.fxml");

        if (loader != null) {
            OperatorMainController controller = loader.getController();
            controller.setUserName(this.lblEmployee.getText());
            controller.setStage(this.stage);
        }
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

    public void setUserName(String userName) {
        lblEmployee.setText(userName);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
