package dk.easv.belmanqcreport.BLL.UTIL;
// Other Imports
import org.mindrot.jbcrypt.BCrypt;

public class Validator {

    public static boolean validatePassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    public static boolean validateUsername(String username) {
        return username != null && !username.trim().isEmpty();
    }
}
