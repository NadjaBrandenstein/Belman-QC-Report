package dk.easv.belmanqcreport.GUI.Model;

import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.BLL.Manager.UserManager;
import javafx.collections.ObservableList;

import java.util.List;

public class UserModel {

    private ObservableList<User> tblEmployee;

    public void searchUser(String query) throws Exception{
        List<User> searchResult = userManager().searchUser(query);
        tblEmployee.clear();
        tblEmployee.addAll(searchResult);
    }

    private UserManager userManager() {
        return userManager();
    }
}
