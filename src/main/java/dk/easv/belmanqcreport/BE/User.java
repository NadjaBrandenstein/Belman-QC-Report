package dk.easv.belmanqcreport.BE;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
    private int userID;
    private String firstName;
    private String lastName;
    private String userType;
    private int userTypeID;
    private BooleanProperty active;

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
        this.active = new SimpleBooleanProperty(true);
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(int userID, String firstName, String lastName, String userType, BooleanProperty active) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.active = active;
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

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean value) {
        active.set(value);
    }

    public BooleanProperty activeProperty() {
        return active;
    }
}
