        package dk.easv.belmanqcreport.GUI.Controller;
        // Project Imports
        import dk.easv.belmanqcreport.BE.Order;
        import dk.easv.belmanqcreport.BE.OrderItem;
        import dk.easv.belmanqcreport.BE.User;
        // Other Imports
        import dk.easv.belmanqcreport.BLL.UTIL.FXMLNavigator;
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
        import javafx.scene.Parent;
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
        import java.io.InputStream;
        import java.net.URL;
        import java.util.List;
        import java.util.Objects;
        import java.util.Optional;
        import java.util.ResourceBundle;


        public class AdminController implements Initializable {


            @FXML
            private ListView<Order> lstOrder;
            @FXML
            private ListView<OrderItem> lstItem;
            @FXML
            private ListView lstLog;

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
            public TableColumn<User, Boolean> colActive;
            @FXML
            private MFXButton btnBack;
            @FXML
            private MFXButton btnLogout;

            private UserModel userModel;
            @FXML
            private ImageView logoImage;
            private CreateEditUserController createEditUserController;
            private Stage stage;

            public AdminController() throws Exception {
                userModel = new UserModel();
                createEditUserController = new CreateEditUserController();
            }

            @Override
            public void initialize(URL location, ResourceBundle resources) {

                // Button icon
                setImageViewIcon(logoImage, "/dk/easv/belmanqcreport/Icons/Belman.png");
                btnBack.setText("");
                setButtonIcon(btnBack, "/dk/easv/belmanqcreport/Icons/backbtn.png");
                btnLogout.setText("");
                setButtonIcon(btnLogout, "/dk/easv/belmanqcreport/Icons/logout.png");

                // TabelView
                colId.setCellValueFactory(new PropertyValueFactory<>("userID"));
                colFName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                colLName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                colRole.setCellValueFactory(new PropertyValueFactory<>("userType"));
                colActive.setCellValueFactory(new PropertyValueFactory<>("active"));

                try {
                    tblEmployee.setItems(userModel.getAllUsers());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                tblEmployee.setRowFactory(tv -> new TableRow<User>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);

                        if (user == null || empty) {
                            setStyle("");
                        } else {
                            if (Boolean.FALSE.equals(user.isActive())) {
                                setStyle("-fx-background-color: lightgray;" + "-fx-text-fill: white");
                            } else {
                                setStyle("-fx-background-color: #7fa8c5;" + "-fx-text-fill: black");
                            }
                        }
                    }
                });

                // ListView



                try {
                    lstOrder.setItems(userModel.getOrders());
                    populateOrderItem();
                    lstLog.setItems(userModel.getLog());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // Search
                txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        userModel.searchUser(newValue);
                    } catch (Exception e) {
                        displayError(e);
                        e.printStackTrace();
                    }
                });

                colId.setCellValueFactory(new PropertyValueFactory<>("userID"));
                colFName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                colLName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                colRole.setCellValueFactory(new PropertyValueFactory<>("userType"));

                // Context menu

                ContextMenu contextMenu = new ContextMenu();

                MenuItem qcRole = new MenuItem("qc");
                MenuItem operatorRole = new MenuItem("operator");
                MenuItem adminRole = new MenuItem("admin");
                MenuItem createUser = new MenuItem("Create User");
                MenuItem editUser = new MenuItem("Edit User");
                MenuItem inactivateUser = new MenuItem("Inactivate User");
                MenuItem activateUser = new MenuItem("Activate User");
                MenuItem createManualLogin = new MenuItem("Create manual Login");
                MenuItem createQRLogin = new MenuItem("Create QR Login");
                MenuItem createChipLogin = new MenuItem("Create Chip Login");

                createChipLogin.setDisable(true);
                createQRLogin.setDisable(true);
                createManualLogin.setDisable(true);

                Menu activateOptions = new Menu("Activate Options");
                Menu assingRole = new Menu("Assign Role");
                Menu loginOptions = new Menu("Login Options");
                Menu usersOptions = new Menu("User Options");

                activateOptions.getItems().addAll(activateUser,inactivateUser);
                usersOptions.getItems().addAll(createUser, editUser, activateOptions);
                loginOptions.getItems().addAll(createQRLogin,createManualLogin,createChipLogin);
                assingRole.getItems().addAll(qcRole,operatorRole,adminRole);
                contextMenu.getItems().addAll(assingRole,usersOptions,loginOptions);

                tblEmployee.setContextMenu(contextMenu);

                // action on the context menu
                inactivateUser.setOnAction((ActionEvent event) -> {
                    User SelectedUser = tblEmployee.getSelectionModel().getSelectedItem();
                    if (SelectedUser != null) {
                        try {
                            deleteUser();
                        } catch (Exception e) {
                            displayError(e);
                        }
                    }
                });

                activateUser.setOnAction((ActionEvent event) -> {
                    User SelectedUser = tblEmployee.getSelectionModel().getSelectedItem();
                    if (SelectedUser != null) {
                        try {
                            activateUser();
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
                    User selectedUser = tblEmployee.getSelectionModel().getSelectedItem();
                    setUserRole(selectedUser, "qc");
                });

                operatorRole.setOnAction((ActionEvent event) -> {
                    User selectedUser = tblEmployee.getSelectionModel().getSelectedItem();
                    setUserRole(selectedUser, "operator");
                });

                adminRole.setOnAction((ActionEvent event) -> {
                    User selectedUser = tblEmployee.getSelectionModel().getSelectedItem();
                    setUserRole(selectedUser, "admin");
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

            public void populateOrderItem() throws Exception {
                List<Order> orders = List.of();

                lstItem.setItems(userModel.getOrderItems(orders.toString()));
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

            private void setUserRole(User user, String newRole) {
                if (!Objects.equals(user.getUserType(), newRole)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Change Role");
                    alert.setHeaderText("Change Role");
                    alert.setContentText("Are you sure you want to change role to " + newRole + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            // Set new role string and corresponding role ID
                            user.setUserType(newRole);
                            user.setUserTypeID(roleToInt(newRole));

                            // Update user via UserModel
                            userModel.updateUser(user);

                            tblEmployee.refresh();

                            Alert success = new Alert(Alert.AlertType.INFORMATION);
                            success.setTitle("Success");
                            success.setHeaderText(null);
                            success.setContentText("User role updated to: " + newRole);
                            success.showAndWait();

                        } catch (Exception e) {
                            displayError(e);
                        }
                    }
                } else {
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("No Change");
                    info.setHeaderText(null);
                    info.setContentText("User already has role: " + newRole);
                    info.showAndWait();
                }
            }


            public int roleToInt(String role) {
                return switch (role.toLowerCase()) {
                    case "admin" -> 1;
                    case "operator" -> 2;
                    case "qc" -> 3;
                    default -> 0; // or throw exception
                };
            }


            private void createUser() throws Exception {
                FXMLNavigator.getInstance().navigateTo(stage, "dk/easv/belmanqcreport/FXML/CreateEditUser.fxml");

                tblEmployee.setItems(userModel.getAllUsers()); // Refresh after creation
            }


            private void editUser() throws Exception {
                User selectedUser = tblEmployee.getSelectionModel().getSelectedItem();
                if(selectedUser != null) {
                    FXMLLoader loader = FXMLNavigator.getInstance().navigateTo(stage, "dk/easv/belmanqcreport/FXML/CreateEditUser.fxml");

                    if (loader != null) {
                        CreateEditUserController controller = loader.getController();
                        controller.setStage(stage);
                        controller.setUserForEdit(selectedUser);
                    }
                    tblEmployee.setItems(userModel.getAllUsers());
                }
            }


            private void deleteUser() throws Exception {
                User selectedUser = tblEmployee.getSelectionModel().getSelectedItem();

                if(selectedUser != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Confirmation");
                    alert.setContentText("Are you sure you want this user inactive");

                    ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
                    if(result == ButtonType.OK) {
                        userModel.deleteUser(selectedUser);
                        tblEmployee.getItems().remove(selectedUser);
                    }
                }
            }

            private void activateUser() throws Exception {
                User selectedUser = tblEmployee.getSelectionModel().getSelectedItem();
                if(selectedUser != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Confirmation");
                    alert.setContentText("Are you sure you want to activate this user");

                    ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
                    if(result == ButtonType.OK) {
                        userModel.activateUser(selectedUser);
                        tblEmployee.getItems().remove(selectedUser);
                    }
                }
            }

            public void setStage(Stage stage) {
                this.stage = stage;
            }

            public void setFirstNameAndLastName(String text) {
                lblEmployee.setText(text);
            }
        }
