package pl.camp.it.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.camp.it.dao.IOrderDAO;
import pl.camp.it.dao.IProductDAO;
import pl.camp.it.model.Order;
import pl.camp.it.model.OrderState;
import pl.camp.it.model.Product;
import pl.camp.it.model.User;
import pl.camp.it.services.IOrderServices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderServicesImpl implements IOrderServices {
    @Autowired
    IOrderDAO orderDAO;
    @Autowired
    IProductDAO productDAO;

    private int returnId;

    @Override
    public int getReturnId() {
        return returnId;
    }

    @Override
    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    @Override
    public void addOrderToFile(User user, List<Product> productList){
        Order order=new Order();
        order.setId(new Random().nextInt());
        order.setLocalDate(LocalDate.now());
        order.setOrderState(OrderState.PRZYJETO_DO_REALIZACJI);
        order.setUser(user);
        order.setProductList(productList);
        orderDAO.persistOrder(order);
    }

    @Override
    public List<Product> upgradeProductDataBase(List<Product> productList){
        List<Product> tempListProduct = productDAO.getListProduct();
        List<Product> temp = new ArrayList<>();

        if (productList!=null) {
            for (Product product : productList) {
                for (Product product1 : tempListProduct) {
                    if (product.getId() == product1.getId()) {
                        if (product1.getAvailability() - product.getAvailability() < 0) {
                            temp.add(product);
                            break;
                        }
                    }
                }
            }
        }
        return temp;
    }

    @Override
    public void saveUpgradeProductDataBase(List<Product> productList){
        List<Product> tempListProduct = productDAO.getListProduct();

        for(Product product:productList){
            for(Product product1: tempListProduct){
                if(product.getId()==product1.getId()){
                    product1.setAvailability(product1.getAvailability()-product.getAvailability());
                    if (product1.getAvailability()==0){
                        product1.setState(false);
                    }
                }
            }
        }
        productDAO.saveChangeProduct(tempListProduct);
    }

    @Override
    public List<Order> returnOrderUser(int idUser, List<Order> orders){
        List<Order> tempOrder=new ArrayList<>();

        for(Order temp: orders){
            if(idUser==temp.getUser().getId()){
                tempOrder.add(temp);
            }
        }
        return tempOrder;
    }

    @Override
    public double priceReturnOrderUser(List<Order> orders){
        double price=0;
        for(int i=0; i<=orders.get(0).getProductList().size()-1; i++){
            price=price+(orders.get(0).getProductList().get(i).getAvailability()*orders.get(0).getProductList().get(i).getPrice());
        }
        return price;
    }

    @Override
    public List<Product> inventoryExceeded(List<Product> orderUser, List<Product> availability){
        List<Product> tempolary = new ArrayList<>();
        for (Product orderU: orderUser){
            for(Product availab: availability){
                if (orderU.getId()==availab.getId()){
                    tempolary.add(orderU);
                    break;
                }
            }
        }

        return tempolary;
    }
}
