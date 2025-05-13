package dk.easv.belmanqcreport.GUI.Controller;

import dk.easv.belmanqcreport.BLL.UTIL.FXMLNavigator;
import dk.easv.belmanqcreport.GUI.Model.OrderModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
    private MFXTextField txtSearch;
    @FXML
    private MFXButton btnSearch;

    private Stage stage;
    private OrderModel orderModel;

    public OperatorSearchController() {
        orderModel = new OrderModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Button icon
        setImageViewIcon(logoImage, "/dk/easv/belmanqcreport/Icons/Belman.png");
        btnBack.setText("");
        setButtonIcon(btnBack, "/dk/easv/belmanqcreport/Icons/backbtn.png");
        btnLogout.setText("");
        setButtonIcon(btnLogout, "/dk/easv/belmanqcreport/Icons/logout.png");
        btnSearch.setText("");
        setButtonIcon(btnSearch, "/dk/easv/belmanqcreport/Icons/search.png");


        txtSearch.selectableProperty().addListener((observable, oldValue, newValue) -> {
            try {
                orderModel.getAllOrders();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void btnSearch(ActionEvent actionEvent) {
        String inputOrderNumber = txtSearch.getText().trim();

        if (inputOrderNumber.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter an order number.");
            alert.showAndWait();
            return;
        }

        try {
            if (orderModel.doesOrderExist(inputOrderNumber)) {
                if (stage == null) {
                    stage = (Stage) btnSearch.getScene().getWindow();
                }

                FXMLLoader loader = FXMLNavigator.getInstance().navigateTo(stage, "dk/easv/belmanqcreport/FXML/OperatorMain.fxml");

                if (loader != null) {
                    OperatorMainController controller = loader.getController();
                    controller.setUserName(this.lblEmployee.getText());
                    controller.setStage(this.stage);
                    controller.setOrderNumber(inputOrderNumber); // <-- Send order number
                }

            } else {
                System.out.println("Order not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while searching for the order.");
        }
    }

    public void btnLogout(ActionEvent actionEvent) {
        if (stage == null) {
            stage = (Stage) btnBack.getScene().getWindow();
        }
        FXMLNavigator.getInstance().navigateTo(stage, "dk/easv/belmanqcreport/FXML/Login.fxml");
    }

    public void btnBack(ActionEvent actionEvent) {
        if (stage == null) {
            stage = (Stage) btnBack.getScene().getWindow();
        }
        FXMLNavigator.getInstance().navigateTo(stage, "dk/easv/belmanqcreport/FXML/Login.fxml");
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
