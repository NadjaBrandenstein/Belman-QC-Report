package dk.easv.belmanqcreport.BLL.Manager;

import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.BLL.UTIL.Search;
import dk.easv.belmanqcreport.DAL.Database.UserDAO_DB;

import java.io.IOException;
import java.util.List;

public class UserManager {

    private UserDAO_DB userDAO;
    private Search userSearch;

    public UserManager () throws IOException {
        userDAO = new UserDAO_DB();
        userSearch = new Search();
    }
    public List<User> getAllUser() throws Exception {
        return userDAO.getAllUser();
    }

    public User createUser(User user) throws Exception {
        return userDAO.createUser(user);
    }

    public User updateUser(User user) throws Exception {
        return userDAO.updateUser(user);
    }

    public void deleteUser(User user) throws Exception {
        userDAO.deleteUser(user);
    }

    public List<User> searchUser(String query) throws Exception {
        List<User> allUsers = getAllUser();
        List<User> searchResult = userSearch.search(allUsers, query);
        return searchResult;
    }
}
