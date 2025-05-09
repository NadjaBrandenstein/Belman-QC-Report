package dk.easv.belmanqcreport.GUI.Model;

import dk.easv.belmanqcreport.BE.Orderv2;
import dk.easv.belmanqcreport.BLL.Manager.OrderManager;

import java.util.List;

public class ImageHandlingModel {

    private OrderManager orderManager;


    public ImageHandlingModel() {
        orderManager = new OrderManager();

    }

    public List<Orderv2> getAllOrders() throws Exception {
        return orderManager.getAllOrders();
    }

    public void updateOrder(Orderv2 order) throws Exception {
        orderManager.updateOrders(order);
    }



}
