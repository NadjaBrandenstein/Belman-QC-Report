package dk.easv.belmanqcreport.DAL.Database;
// Project Imports
import dk.easv.belmanqcreport.BE.Login;
import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.Interface.IRepository;
// Other Imports
import org.mindrot.jbcrypt.BCrypt;
// Java Imports
import java.io.IOException;
import java.sql.*;
import java.util.List;


public class LoginRepository implements IRepository<Login> {

    private final DBConnection dbConnection;

    private User user;

    public LoginRepository() throws IOException {
        dbConnection = new DBConnection();
        user = new User();
    }

    @Override
    public List<Login> getAll() throws Exception {
        return List.of();
    }

    @Override
    public Login add(Login login)throws Exception {
        String getUserTypeIdSql = "SELECT userTypeID FROM UserType WHERE userType = ?";
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
            int userTypeId = rs.getInt("userTypeID");

            // Insert into Login table
            insertStmt.setString(1, login.getUsername());
            insertStmt.setString(2, hashedPassword);
            insertStmt.setInt(3, userTypeId);
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return login;
    }

    @Override
    public Login update(Login login) throws Exception{
        String sql = "UPDATE Login SET password = ? WHERE username = ?";
        String hashedPassword = BCrypt.hashpw(login.getPassword(), BCrypt.gensalt());

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hashedPassword);
            stmt.setString(2, login.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return login;
    }

    @Override
    public void delete(Login login) throws Exception {
        String sql = "DELETE FROM Login WHERE username = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login.getUsername());
            stmt.executeUpdate();
        }
    }

    public Login getLoginByUsername(String username) throws Exception {
        String sql = """
        SELECT l.username, l.password, ut.userType, u.fName, u.lName, u.userID
        FROM Login l
        JOIN UserType ut ON l.userTypeID = ut.userTypeID
        JOIN [User] u ON u.userID = l.userID
        WHERE l.username = ?
        """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Login login = new Login();
                login.setUsername(rs.getString("username"));
                login.setPassword(rs.getString("password"));
                login.setUserType(rs.getString("userType"));

                User user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setFirstName(rs.getString("fName"));
                user.setLastName(rs.getString("lName"));

                login.setUser(user);
                return login;
            }
        }
        return null;
    }
}
