package pl.camp.it.model;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private int id;
    private LocalDate localDate;
    private User user;
    private List<Product> productList;
    private OrderState orderState;

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();

        sb.append(this.id).append("<:@:>").append(this.localDate).append("<:@:>").append(this.orderState).
                append("<:@:>").append(this.user).append("<:@:>").append(this.productList);
        return sb.toString();
    }
}
