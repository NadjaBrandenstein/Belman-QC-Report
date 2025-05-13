package dk.easv.belmanqcreport.DAL.Interface;

import dk.easv.belmanqcreport.BE.MyImage;

import java.sql.SQLException;
import java.util.List;

public interface IRepository<T> {
    List<T> getAll() throws Exception;
    T getById(int id) throws Exception;
    T add(T item) throws Exception;
    T update(T item) throws Exception;
    void delete(T item) throws Exception;
}
