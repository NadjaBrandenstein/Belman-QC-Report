package dk.easv.belmanqcreport.BLL.UTIL;

import dk.easv.belmanqcreport.BE.User;

import java.util.ArrayList;
import java.util.List;

public class Search {
    public List<User> search(List<User> searchBase, String query) {
        List<User> searchResult = new ArrayList<>();
        if (query == null || query.isBlank()) {
            return searchBase;
        }

        String lowerQuery = query.toLowerCase();

        for (User user : searchBase) {
            if (matches(user, query)) {
                searchResult.add(user);
            }
        }
        return searchResult;
    }

    private boolean matches(User user, String query) {
        String lowerQuery = query.toLowerCase();

        return (user.getFirstName() != null && user.getFirstName().toLowerCase().contains(lowerQuery)) ||
                (user.getLastName() != null && user.getLastName().toLowerCase().contains(lowerQuery)) ||
                (String.valueOf(user.getOrderID()).contains(lowerQuery));
    }

}