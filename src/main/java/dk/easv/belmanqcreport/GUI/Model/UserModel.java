package dk.easv.belmanqcreport.GUI.Model;

import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.BLL.Manager.UserManager;
import javafx.collections.ObservableList;

import java.util.List;

import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.BLL.Manager.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class UserModel {

    private ObservableList<User> tblEmployee;
    private UserManager userManager;

    public void searchUser(String query) throws Exception{
        List<User> searchResult = userManager.searchUser(query);
        tblEmployee.clear();
        tblEmployee.addAll(searchResult);
    }

    public UserModel() throws IOException {
        userManager = new UserManager();
        tblEmployee = FXCollections.observableArrayList();
    }

    public ObservableList<User> getAllUsers () throws Exception {
        List<User> users = userManager.getAllUser();
        tblEmployee.addAll(users);
        return tblEmployee;
    }

    public void UpdateUser(User user) throws Exception {
        userManager.updateUser(user);
    }

    public void createUser(User user) throws Exception {
        User newUser = userManager.createUser(user);
        tblEmployee.add(newUser);
    }

    public void deleteUser(User user) throws Exception {
        userManager.deleteUser(user);
        tblEmployee.remove(user);
    }

}
