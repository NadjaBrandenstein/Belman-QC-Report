package dk.easv.belmanqcreport.BLL.UTIL;

import dk.easv.belmanqcreport.BE.Login;
import dk.easv.belmanqcreport.DAL.Database.LoginDAO_DB;
import dk.easv.belmanqcreport.DAL.Database.UserDAO_DB;

import java.io.IOException;

public class AuthService {
    private final LoginDAO_DB loginDAO;


    public AuthService() throws IOException {
        loginDAO = new LoginDAO_DB();
    }

    public boolean login(String username, String password) {
        if (!Validator.validateUsername(username)) {
            System.out.println("Invalid username.");
            return false;
        }

        Login user;
        try {
            user = loginDAO.getLoginByUsername(username); // we need to implement this
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }

        if (user == null) {
            System.out.println("User not found.");
            return false;
        }

        if (!Validator.validatePassword(password, user.getPassword())) {
            System.out.println("Incorrect password.");
            return false;
        }

        return true;
    }
}
