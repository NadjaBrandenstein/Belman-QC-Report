package dk.easv.belmanqcreport.BLL.Manager;

import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.BLL.UTIL.Search;
import dk.easv.belmanqcreport.DAL.Database.UserRepository;

import java.util.List;

public class UserManager {
    private final UserRepository userDAO;
    private final Search userSearch;

    public UserManager() throws Exception {
        this.userDAO = new UserRepository();
        this.userSearch = new Search();
    }

    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    public User createUser(User user) {
        userDAO.add(user);
        return user;
    }

    public User updateUser(User user) {
        userDAO.update(user);
        return user;
    }

    public void deleteUser(User user) {
        userDAO.delete(user);
    }

    public List<User> searchUser(String query) {
        List<User> allUsers = userDAO.getAll();
        return userSearch.search(allUsers, query);
    }
}
