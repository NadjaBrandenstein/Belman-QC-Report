package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.Interface.IUser;
import org.bridj.cpp.std.list;

import javax.lang.model.type.ArrayType;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO_DB implements IUser {

    private final DBConnection dbConnection;

    public UserDAO_DB() throws IOException {
        dbConnection = new DBConnection();
    }


    @Override
    public List<User> getAllUser() throws Exception {

        List<User> users = new ArrayList<>();

        String sql = """
        SELECT u.userID,u.fname, u.lname, ut.userType
        FROM [User] u
        JOIN UserType ut on u.userTypeID = ut.userTypeID
        """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int userID = rs.getInt("userID");
                String firstName = rs.getString("fname");
                String lastName = rs.getString("lname");
                String userType = rs.getString("userType");

                User user = new User(userID, firstName, lastName, userType);
                users.add(user);
            }
        }
    return users;
    }

    @Override
    public User createUser(User user) throws Exception {
        String sql = "INSERT INTO [User] (fname, lname, userTypeID) VALUES (?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getUserTypeID());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserID(generatedKeys.getInt(1));
                    return user;
                } else {
                    throw new Exception("Failed to insert user, no ID obtained.");
                }
            }
        }
    }


    @Override
    public User updateUser(User user) throws Exception {
        String sql = "UPDATE [User] SET fname = ?, lname = ?, userTypeID = ? WHERE userID = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getUserTypeID());
            ps.setInt(4, user.getUserID());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new Exception("User not found or not updated.");
            }

            return user;
        }
    }


    @Override
    public void deleteUser(User user) throws Exception {
        String sql = "DELETE FROM [User] WHERE userID = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getUserID());
            ps.executeUpdate();
        }
    }

}
