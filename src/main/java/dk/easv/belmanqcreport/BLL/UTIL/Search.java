package dk.easv.belmanqcreport.BLL.UTIL;

import dk.easv.belmanqcreport.BE.Order;

import java.util.ArrayList;
import java.util.List;

public class Search {
    public List<Order> search(List<Order> searchBase, String query) {
        List<Order> searchResult = new ArrayList<>();

        for(Order order : searchBase) {
            if(compareToFirstName(query, order) || compareToLastName(query, order) ||compareToOrderID(Integer.parseInt(query), order)){
                searchResult.add(order);
            }
        }
        return searchResult;
    }
    private boolean compareToFirstName(String query, Order order) {
        return order.getFirstname().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToLastName(String query, Order order) {
        return order.getLastname().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToOrderID(int query, Order order) {
        return String.valueOf(order.getOrderID()).contains(String.valueOf(query));
    }
}
