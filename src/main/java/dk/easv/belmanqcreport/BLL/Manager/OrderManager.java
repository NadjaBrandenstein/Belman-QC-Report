package dk.easv.belmanqcreport.BLL.Manager;

import dk.easv.belmanqcreport.BE.Orderv2;
import dk.easv.belmanqcreport.DAL.Database.OrderDAO_DB;

import java.util.List;

public class OrderManager {

    private OrderDAO_DB orderDAO;

    public OrderManager() {

        orderDAO = new OrderDAO_DB();

    }

    public List<Orderv2> getAllOrders() throws Exception {
        return orderDAO.getAllOrder();
    }
    public Orderv2 createOrders(Orderv2 order) throws Exception {
        return orderDAO.createOrder(order);
    }
    public Orderv2 updateOrders(Orderv2 order) throws Exception {
        return orderDAO.updateOrder(order);
    }
    public void deleteOrders(Orderv2 order) throws Exception {
        orderDAO.deleteOrder(order);
    }

}
