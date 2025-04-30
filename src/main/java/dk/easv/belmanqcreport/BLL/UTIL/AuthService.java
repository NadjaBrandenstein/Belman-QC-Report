package dk.easv.belmanqcreport.BLL.UTIL;

import dk.easv.belmanqcreport.BE.Login;
import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.DAL.Database.LoginDAO_DB;
import dk.easv.belmanqcreport.DAL.Database.UserDAO_DB;

import java.io.IOException;

public class AuthService {
    private final LoginDAO_DB loginDAO;
    private final UserDAO_DB userDAO;

    public AuthService() throws IOException {
        this.loginDAO = new LoginDAO_DB();
        this.userDAO = new UserDAO_DB();
    }

    public Login login(String username, String password) {
        if (!Validator.validateUsername(username)) {
            System.out.println("Invalid username.");
            return null;
        }

        try {
            Login login = loginDAO.getLoginByUsername(username);
            if (login == null) {
                System.out.println("User not found.");
                return null;
            }

            if (!Validator.validatePassword(password, login.getPassword())) {
                System.out.println("Incorrect password.");
                return null;
            }

            login.setUserType(login.getUserType()); // Assuming UserType is part of the User object
            return login;

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            return null;
        }
    }

}
