package dk.easv.belmanqcreport.BLL.UTIL;

import dk.easv.belmanqcreport.BE.User;

import java.util.ArrayList;
import java.util.List;

public class Search {
    public List<User> search(List<User> searchBase, String query) {
        List<User> searchResult = new ArrayList<>();

        for(User user : searchBase) {
            if(compareToFirstName(query, user) || compareToLastName(query, user) ||compareToOrderID(Integer.parseInt(query), user)){
                searchResult.add(user);
            }
        }
        return searchResult;
    }
    private boolean compareToFirstName(String query, User order) {
        return order.getFirstname().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToLastName(String query, User order) {
        return order.getLastname().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToOrderID(int query, User order) {
        return String.valueOf(order.getOrderID()).contains(String.valueOf(query));
    }
}
