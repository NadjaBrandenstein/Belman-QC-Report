package dk.easv.belmanqcreport.BLL.UTIL;

import dk.easv.belmanqcreport.BE.Login;
import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.DAL.Database.LoginDAO_DB;
import dk.easv.belmanqcreport.DAL.Database.UserDAO_DB;
import dk.easv.belmanqcreport.GUI.Controller.LoginController;

import java.io.IOException;

public class AuthService {
    private final LoginDAO_DB loginDAO;
    private final UserDAO_DB userDAO;
    private LoginController loginController;

    public AuthService() throws IOException {
        this.loginDAO = new LoginDAO_DB();
        this.userDAO = new UserDAO_DB();
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
            Login login = loginDAO.getLoginByUsername(username);
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
            return null;
        }
    }
}
