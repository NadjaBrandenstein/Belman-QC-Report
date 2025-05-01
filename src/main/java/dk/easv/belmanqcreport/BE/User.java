package dk.easv.belmanqcreport.BE;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
    private int userID;
    private String firstName;
    private String lastName;
    private String userType;
    private int userTypeID;

    public User(int userID, String firstName, String lastName, String userType) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }
    private ObservableList<User> users = FXCollections.observableArrayList();


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

    public int getUserTypeID() {
        return userTypeID;
    }

    public void setUserTypeID(int userTypeID) {
        this.userTypeID = userTypeID;
    }

    public String getFirstname() {
        return getFirstName().toLowerCase();
    }

    public String getLastname() {
        return getLastName().toLowerCase();
    }

    public int getOrderID() {
        return userID;
    }
}
