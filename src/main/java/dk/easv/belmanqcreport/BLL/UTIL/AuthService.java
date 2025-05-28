package dk.easv.belmanqcreport.BLL.UTIL;
// Project Imports
import dk.easv.belmanqcreport.BE.Login;
import dk.easv.belmanqcreport.DAL.Database.LoginRepository;
import dk.easv.belmanqcreport.DAL.Database.UserRepository;
import dk.easv.belmanqcreport.GUI.Controller.LoginController;
import dk.easv.belmanqcreport.GUI.Controller.QcController;
// Java Imports
import java.io.IOException;

public class AuthService {
    public LoginRepository loginRepository;
    private final UserRepository userRepository;
    public LoginController loginController;
    public QcController qcController;

    public AuthService() throws IOException {
        this.loginRepository = new LoginRepository();
        this.userRepository = new UserRepository();
        this.qcController = new QcController();
    }

    public void setLoginController(LoginController controller) {
        this.loginController = controller;
    }

    public Login login(String username, String password) throws IOException {

        if (!Validator.validateUsername(username)) {
            System.out.println("Invalid username.");
            loginController.lblLoginStatus.setText("Incorrect username or password");
            return null;
        }

        try {
            Login login = loginRepository.getLoginByUsername(username);
            if (login == null) {
                System.out.println("User not found.");
                loginController.lblLoginStatus.setText("Incorrect username or password");
                return null;
            }

            if (!Validator.validatePassword(password, login.getPassword())) {
                System.out.println("Incorrect password.");
                loginController.lblLoginStatus.setText("Incorrect username or password");

                return null;
            }

            login.setUserType(login.getUserType());
            return login;

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            loginController.lblLoginStatus.setText("Unexpected error contact system admin");
            return null;
        }
    }
}
