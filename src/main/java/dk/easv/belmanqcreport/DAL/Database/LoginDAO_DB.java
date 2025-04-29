package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.Login;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.ILogin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class LoginDAO_DB implements ILogin {

    private final DBConnection dbConnection;


    public LoginDAO_DB() throws IOException {
        dbConnection = new DBConnection();
    }


    @Override
    public List<Login> getAllLogin() throws Exception {
        List<Login> logins = new ArrayList<>();
        String sql = "SELECT username FROM Login";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Login login = new Login(rs.getString("username"), null);
                logins.add(login);
            }
        }
        return logins;
    }


    @Override
    public Login createLogin(Login login) throws Exception {
        String getUserTypeIdSql = "SELECT id FROM UserType WHERE userType = ?";
        String insertLoginSql = "INSERT INTO Login (username, password, UserTypeID) VALUES (?, ?, ?)";

        String hashedPassword = BCrypt.hashpw(login.getPassword(), BCrypt.gensalt());

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement userTypeStmt = conn.prepareStatement(getUserTypeIdSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertLoginSql, Statement.RETURN_GENERATED_KEYS)) {

            // Get UserTypeID based on the provided type (e.g., "Admin" or "Event")
            userTypeStmt.setString(1, login.getUserType());
            ResultSet rs = userTypeStmt.executeQuery();
            if (!rs.next()) {
                throw new Exception("UserType not found: " + login.getUserType());
            }
            int userTypeId = rs.getInt("id");

            // Insert into Login table
            insertStmt.setString(1, login.getUsername());
            insertStmt.setString(2, hashedPassword);
            insertStmt.setInt(3, userTypeId);
            insertStmt.executeUpdate();
        }

        return login;
    }



    @Override
    public Login updateLogin(Login login) throws Exception {
        String sql = "UPDATE Login SET password = ? WHERE username = ?";
        String hashedPassword = BCrypt.hashpw(login.getPassword(), BCrypt.gensalt());

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hashedPassword);
            stmt.setString(2, login.getUsername());
            stmt.executeUpdate();
        }
        return login;
    }


    @Override
    public void deleteLogin(Login login) throws Exception {
        String sql = "DELETE FROM Login WHERE username = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login.getUsername());
            stmt.executeUpdate();
        }
    }

    public Login getLoginByUsername(String username) throws Exception {
        String sql = "SELECT username, password, UserTypeID FROM Login WHERE username = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String user = rs.getString("username");
                String hashedPassword = rs.getString("password");
                // Assuming you want to get userType too:
                String userType = getUserTypeById(rs.getInt("UserTypeID"));
                return new Login(user, hashedPassword, userType);
            }
        }

        return null;
    }

    private String getUserTypeById(int id) throws Exception {
        String sql = "SELECT userType FROM UserType WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("userType");
            }
        }
        return null;
    }
}
