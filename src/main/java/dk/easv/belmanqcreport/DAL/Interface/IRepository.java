package dk.easv.belmanqcreport.DAL.Interface;

import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    List<T> getAll() throws Exception;
    T add(T item) throws Exception;
    T update(T item) throws Exception;
    void delete(T item) throws Exception;

}
