package dk.easv.belmanqcreport.BE;

public class User {
    private int userID;
    private String firstName;
    private String lastName;
    private int userType;

    private User(int userID, String firstName, String lastName, int userType) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }

    private int getUserID() {
        return userID;
    }

    private void setUserID(int userID) {
        this.userID = userID;
    }

    private String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private int getUserType() {
        return userType;
    }

    private void setUserType(int userType) {
        this.userType = userType;
    }
}
