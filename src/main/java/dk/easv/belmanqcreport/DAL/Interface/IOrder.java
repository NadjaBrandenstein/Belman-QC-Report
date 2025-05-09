package dk.easv.belmanqcreport.DAL.Interface;

import dk.easv.belmanqcreport.BE.Orderv2;

import java.util.List;

public interface IOrder {
    List<Orderv2> getAllOrder() throws Exception;
    Orderv2 createOrder(Orderv2 order) throws Exception;
    Orderv2 updateOrder(Orderv2 order) throws Exception;
    void deleteOrder(Orderv2 order) throws Exception;
}
