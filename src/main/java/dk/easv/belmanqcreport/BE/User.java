package dk.easv.belmanqcreport.BE;
// JavaFX Imports
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class User {
    private int userID;
    private String firstName;
    private String lastName;
    private String userType;
    private int userTypeID;
    private BooleanProperty active;

    // Called in AuthServiceTest
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    // Called in UserRepository
    public User(int userID, String firstName, String lastName, String userType, BooleanProperty active) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.active = active;
    }

    public User(String userType) {
        this.userType = userType;
    }

    public User() {
        this.active = new SimpleBooleanProperty(true);
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

    public int getUserTypeID() {
        return userTypeID;
    }

    public void setUserTypeID(int userTypeID) {
        this.userTypeID = userTypeID;
    }

    public int getOrderID() {
        return userID;
    }

    public boolean isActive() {
        return active.get();
    }

}
