package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.Interface.IRepository;

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
            SELECT u.userID, u.fname, u.lname, ut.userType
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

                User user = new User(userID, firstName, lastName, userType);
                users.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Consider logging instead in production
        }

        return users;
    }

    @Override
    public User getById(int id) {
        String sql = """
            SELECT u.userID, u.fname, u.lname, ut.userType
            FROM [User] u
            JOIN UserType ut ON u.userTypeID = ut.userTypeID
            WHERE u.userID = ?
        """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("userID"),
                            rs.getString("fname"),
                            rs.getString("lname"),
                            rs.getString("userType")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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
        String sql = "DELETE FROM [User] WHERE userID = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getUserID());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

