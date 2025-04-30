package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.DAL.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO_DB {

    private final DBConnection dbConnection;

    public UserDAO_DB() throws IOException {
        dbConnection = new DBConnection();
    }

    public User getUserByUsername(String username) throws Exception {
        String sql = "SELECT userType FROM User WHERE username = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserType(rs.getString("userType"));
                return user;
            }
        }
        return null;
    }

}
