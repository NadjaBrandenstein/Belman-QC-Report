package dk.easv.belmanqcreport;

import dk.easv.belmanqcreport.BE.Login;
import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.GUI.Controller.LoginController;
import dk.easv.belmanqcreport.BLL.UTIL.AuthService;
import dk.easv.belmanqcreport.DAL.Database.LoginRepository;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    AuthService authService;

    // Fake Label class to replace JavaFX Label
    static class FakeLabel {
        private String text;
        public void setText(String text) { this.text = text; }
        public String getText() { return text; }
    }

    // Fake LoginRepository to simulate database
    class TestLoginRepo extends LoginRepository {
        public TestLoginRepo() throws IOException {}

        @Override
        public Login getLoginByUsername(String username) {
            if (username.equals("validUser")) {
                Login login = new Login();
                login.setUsername("validUser");

                String hashed = BCrypt.hashpw("1234", BCrypt.gensalt());
                login.setPassword(hashed);

                login.setUserType("admin");
                login.setUser(new User("Test", "User"));
                return login;
            }
            return null;
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        JavaFxInitializer.init();

        authService = new AuthService() {
            {
                this.loginRepository = new TestLoginRepo();
            }
        };
        LoginController testController = new LoginController();
        testController.lblLoginStatus = new Label();
        authService.setLoginController(testController);
    }


    @Test
    void testInvalidUsername() throws Exception {
        Login result = authService.login("", "1234");
        assertNull(result);
        assertEquals("Incorrect username or password", authService.loginController.lblLoginStatus.getText());
    }

    @Test
    void testUserNotFound() throws Exception {
        Login result = authService.login("unknownUser", "1234");
        assertNull(result);
        assertEquals("Incorrect username or password", authService.loginController.lblLoginStatus.getText());
    }

    @Test
    void testWrongPassword() throws Exception {
        Login result = authService.login("validUser", "wrong");
        assertNull(result);
        assertEquals("Incorrect username or password", authService.loginController.lblLoginStatus.getText());
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        Login result = authService.login("validUser", "1234");
        assertNotNull(result);
        assertEquals("validUser", result.getUsername());
        assertEquals("admin", result.getUserType());
        assertEquals("Test", result.getUser().getFirstName());
    }
}
