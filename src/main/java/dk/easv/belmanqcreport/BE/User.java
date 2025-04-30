package dk.easv.belmanqcreport.BE;

public class User {
    private int userID;
    private String firstName;
    private String lastName;
    private String userType;

    public User(int userID, String firstName, String lastName, String userType) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }

    public User(String userType) {
        this.userType = userType;
    }

    public User() {

    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
