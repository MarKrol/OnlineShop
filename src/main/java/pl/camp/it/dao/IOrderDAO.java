package pl.camp.it.dao;

import pl.camp.it.model.Order;
import pl.camp.it.model.OrderState;

import java.util.List;

public interface IOrderDAO {
    void persistOrder(Order order);
    List<Order> getListOrder();
    void saveChangeOrder(List<Order> orderList);
}
