package dk.easv.belmanqcreport.GUI.Model;

import dk.easv.belmanqcreport.BE.Order;
import dk.easv.belmanqcreport.BE.OrderItem;
import dk.easv.belmanqcreport.BE.User;
import dk.easv.belmanqcreport.BLL.Manager.OrderManager;
import dk.easv.belmanqcreport.BLL.Manager.UserManager;
import javafx.collections.ObservableList;

import java.util.List;


import javafx.collections.FXCollections;


public class UserModel {

    private ObservableList<User> tblEmployee;
    private ObservableList<OrderItem> lstOrderItem;
    private ObservableList<Order> lstOrder;
    private ObservableList<OrderItem> lstLog;

    private UserManager userManager;
    private OrderManager orderManager;

    public UserModel() throws Exception {
        userManager = new UserManager();
        orderManager = new OrderManager();
        tblEmployee = FXCollections.observableArrayList();
        lstOrderItem = FXCollections.observableArrayList();
        lstOrder = FXCollections.observableArrayList();
        lstLog = FXCollections.observableArrayList();
    }

    public void searchUser(String query) throws Exception{
        List<User> searchResult = userManager.searchUser(query);
        tblEmployee.clear();
        tblEmployee.addAll(searchResult);
    }


    public ObservableList<User> getAllUsers () throws Exception {
        List<User> users = userManager.getAllUsers();
        tblEmployee.clear();
        tblEmployee.addAll(users);
        return tblEmployee;
    }


    public ObservableList<Order> getOrders () throws Exception {
        List<Order> orders = orderManager.getAllOrders();
        lstOrder.clear();
        lstOrder.addAll(orders);
        return lstOrder;
    }

    public ObservableList<OrderItem> getOrderItems (String orderNumber) throws Exception {
        List<OrderItem> orderItem = orderManager.getItemsByOrderNumber(orderNumber);
        lstOrderItem.clear();
        lstOrderItem.addAll(orderItem);
        return lstOrderItem;
    }

    public ObservableList<OrderItem> getLog () throws Exception {
        lstLog.clear();
        return lstLog;
    }

    public void updateUser(User user) throws Exception {
        userManager.updateUser(user);
    }

    public void createUser(User user) throws Exception {
        User newUser = userManager.createUser(user);
        tblEmployee.add(newUser);
    }

    public void deleteUser(User user) throws Exception {
        userManager.deleteUser(user);
        tblEmployee.remove(user);
    }

    public void activateUser(User user) throws Exception {
        userManager.activate(user);
    }

}
