package pl.camp.it.services;

import pl.camp.it.model.Product;
import pl.camp.it.model.User;

import java.util.List;

public interface IUserServices {
    boolean userInDataBaseByName(String name);
    boolean matchingPasswords(String pass1, String pass2);
    void addUserInDataBase(User user);
    boolean login(User user);
    String errorMessage();
    User getOnlyUser(User user);
    List<Product> filtrProducts(List<Product> list1, String filter);
}
