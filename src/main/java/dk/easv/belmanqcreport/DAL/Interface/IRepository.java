package dk.easv.belmanqcreport.DAL.Interface;

import java.util.List;

public interface IRepository<T> {
    List<T> getAll();
    T getById(int id);
    void add(T item);
    void update(T item);
    void delete(T item);
}
