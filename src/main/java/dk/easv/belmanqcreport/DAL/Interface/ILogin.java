package dk.easv.belmanqcreport.DAL.Interface;

import java.util.List;
import dk.easv.belmanqcreport.BE.Login;

public interface ILogin {

    List<Login> getAllLogin() throws Exception;
    Login createLogin(Login login) throws Exception;
    Login updateLogin(Login login) throws Exception;
    void deleteLogin(Login login) throws Exception;

}
