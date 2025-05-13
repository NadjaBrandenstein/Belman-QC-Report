package dk.easv.belmanqcreport.BLL.UTIL;

import dk.easv.belmanqcreport.BE.Login;
import dk.easv.belmanqcreport.DAL.Database.LoginRepository;

public class LoginInitializer {

    private final LoginRepository loginRepository;

    public LoginInitializer() throws Exception {
        loginRepository = new LoginRepository();
    }

    public void createDefaultLogins() {
        try {
            // QC login
            Login qcLogin = new Login("qc", "qc", "qc");
            loginRepository.add(qcLogin);

            // Operator login
            Login operatorLogin = new Login("operator", "operator", "operator");
            loginRepository.add(operatorLogin);

            // Admin login
            Login adminLogin = new Login("admin", "admin", "admin");
            loginRepository.add(adminLogin);

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
