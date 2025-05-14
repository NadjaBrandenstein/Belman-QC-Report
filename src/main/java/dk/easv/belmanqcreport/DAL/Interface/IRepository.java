package dk.easv.belmanqcreport.DAL.Interface;

import java.util.List;

public interface IRepository<T> {
    List<T> getAll() throws Exception;
    T add(T item) throws Exception;
    T update(T item) throws Exception;
    void delete(T item) throws Exception;
}
