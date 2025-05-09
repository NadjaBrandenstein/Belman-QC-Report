package dk.easv.belmanqcreport.DAL.Interface;

import dk.easv.belmanqcreport.BE.User;

import java.util.List;

public interface IUser {

    List<User> getAllUser() throws Exception;
    User createUser(User user) throws Exception;
    User updateUser(User user) throws Exception;
    void deleteUser(User user) throws Exception;
}
