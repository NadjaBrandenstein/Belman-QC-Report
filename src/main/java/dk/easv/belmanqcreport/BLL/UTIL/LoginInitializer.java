package dk.easv.belmanqcreport.BLL.UTIL;

import dk.easv.belmanqcreport.BE.Login;
import dk.easv.belmanqcreport.DAL.Database.LoginDAO_DB;

public class LoginInitializer {

    private final LoginDAO_DB loginDAO;

    public LoginInitializer() throws Exception {
        loginDAO = new LoginDAO_DB();
    }

    public void createDefaultLogins() {
        try {
            // QC login
            Login qcLogin = new Login("qc", "qc", "qc");
            loginDAO.createLogin(qcLogin);

            // Operator login
            Login operatorLogin = new Login("operator", "operator", "operator");
            loginDAO.createLogin(operatorLogin);

            // Admin login
            Login adminLogin = new Login("admin", "admin", "admin");
            loginDAO.createLogin(adminLogin);

            System.out.println("Default logins created successfully.");

        } catch (Exception e) {
            System.err.println("Error creating default logins: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            LoginInitializer initializer = new LoginInitializer();
            initializer.createDefaultLogins();
        } catch (Exception e) {
            System.err.println("Initialization failed: " + e.getMessage());
        }
    }
}
