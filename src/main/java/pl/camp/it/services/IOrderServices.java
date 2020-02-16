package pl.camp.it.services;

import pl.camp.it.model.Order;
import pl.camp.it.model.Product;
import pl.camp.it.model.User;

import java.util.List;

public interface IOrderServices {
    void addOrderToFile(User user, List<Product> productList);
    boolean upgradeProductDataBase(List<Product> productList);
    void saveUpgradeProductDataBase(List<Product> productList);
    List<Order> returnOrderUser(int idUser, List<Order> orders);
    double priceReturnOrderUser(List<Order> orders);
    int getReturnId();
    void setReturnId(int returnId);

}
