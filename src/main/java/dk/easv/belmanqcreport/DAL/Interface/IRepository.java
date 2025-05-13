package dk.easv.belmanqcreport.DAL.Interface;

import dk.easv.belmanqcreport.BE.MyImage;

import java.sql.SQLException;
import java.util.List;

public interface IRepository<T> {
    List<T> getAll();
    T getById(int id) throws SQLException;
    MyImage add(T item) throws SQLException;
    MyImage update(T item);
    void delete(T item);
}
