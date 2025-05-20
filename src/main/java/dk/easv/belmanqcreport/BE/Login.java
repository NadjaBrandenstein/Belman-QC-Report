package dk.easv.belmanqcreport.BE;

public class Login {
    private String username;
    private String password;
    private String userType;
    private User user;


    public Login(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public Login(String username, String password) {
    }

    public Login() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
