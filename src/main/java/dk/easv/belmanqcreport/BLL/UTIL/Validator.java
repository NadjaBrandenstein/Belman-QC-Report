package dk.easv.belmanqcreport.BLL.UTIL;

import org.mindrot.jbcrypt.BCrypt;

public class Validator {

    public static boolean validatePassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    public static boolean validateUsername(String username) {
        return username != null && !username.trim().isEmpty();
    }

    public static boolean validateEmailFormat(String email) {
        // You can adapt the regex depending on how strict you want to be
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$");
    }
}
