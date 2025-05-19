package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.Interface.IRepository;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IRepository<User> {

    private final DBConnection dbConnection;

    public UserRepository() throws IOException {
        dbConnection = new DBConnection();
    }


    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = """
                SELECT u.userID, u.fname, u.lname, ut.userType, u.is_active
                FROM [User] u
                JOIN UserType ut ON u.userTypeID = ut.userTypeID
            """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int userID = rs.getInt("userID");
                String firstName = rs.getString("fname");
                String lastName = rs.getString("lname");
                String userType = rs.getString("userType");
                boolean isActive = rs.getBoolean("is_active");
                User user = new User(userID, firstName, lastName, userType, new SimpleBooleanProperty(isActive));

                users.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }


    @Override
    public User add(User user) {
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
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE [User] SET fname = ?, lname = ?, userTypeID = ? WHERE userID = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getUserTypeID());
            ps.setInt(4, user.getUserID());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(User user) {
        String sql = "UPDATE [User] SET is_active = 0 WHERE userID = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getUserID());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void activate(User user) {
        String sql = "UPDATE [User] SET is_active = 1 WHERE userID = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getUserID());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

