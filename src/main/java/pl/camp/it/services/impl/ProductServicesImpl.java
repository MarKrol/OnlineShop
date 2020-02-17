package pl.camp.it.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.camp.it.dao.IProductDAO;
import pl.camp.it.model.Order;
import pl.camp.it.model.Product;
import pl.camp.it.services.IProductServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ProductServicesImpl implements IProductServices {

    @Autowired
    IProductDAO productDAO;

    @Override
    public Product findProductById(int id){
        List<Product> tempProduct = productDAO.getListProduct();

        Product tempolary = new Product();

        for (Product temp : tempProduct){
            if (temp.getId()==id){
                tempolary=temp;
                break;
            }
        }
        return  tempolary;
    }

    @Override
    public Product completeProduct(Product product){

        product.setId(randomId());

        if (product.getAvailability()!=0) {
            product.setState(true);
        } else {
            product.setState(false);
        }
        return product;
    }

    @Override
    public void saveChangeToFile1(Product product){
        List<Product> listProduct=productDAO.getListProduct();
        List<Product> tempolary=new ArrayList<>();

        if (product.getAvailability()==0){
            product.setState(false);
        } else{
            product.setState(true);
        }

        for (Product tempProduct:listProduct){
            if (tempProduct.getId()!=product.getId()){
                tempolary.add(tempProduct);
            } else{
                tempolary.add(product);
            }
        }
        productDAO.saveChangeProduct(tempolary);
    }

    @Override
    public List<Product> showAvailableProducts(List<Product> productListDB, List<Product> sessionObject, String role){
        List<Product> tempListProducts = new ArrayList<>();

        List<Product> temp=new ArrayList<>();

        if(productListDB!=null){
            if (sessionObject!=null) {
                tempListProducts=sessionObject;
            }
            if(role =="USER" || role=="" || role==null) {
                for (Product product : productListDB) {
                    for (int i = 0; i <= tempListProducts.size() - 1; i++) {
                        if (product.getId() == tempListProducts.get(i).getId()) {
                            if (product.getAvailability() - tempListProducts.get(i).getAvailability() <= 0) {
                                product.setState(false);
                                break;
                            } else {
                                product.setState(true);
                            }
                        }
                    }
                }
            }

            if(role =="USER" || role=="" || role==null) {
                for (Product product : productListDB) {
                    if (product.isState()) {
                        temp.add(product);
                    }
                }
            }else{
                temp=productListDB;
            }

        }
        return temp;
    }

    @Override
    public Product rememberProducers(Product product){
        List<Product> productList=productDAO.getListProduct();

        Product tempProduct=new Product();

        for(Product temp: productList) {
            if (temp.getId()==product.getId()) {
                if (temp.getAvailability()>=product.getAvailability()){
                    temp.setAvailability(product.getAvailability());
                    if (temp.getAvailability()==0){
                        temp.setState(false);
                    }else {
                        temp.setAvailability(product.getAvailability());
                        temp.setState(true);
                    }
                    tempProduct=temp;
                }
            break;
            }
        }
        return tempProduct;
    }

    @Override
    public boolean isAvailableProduct(Product product, List<Product> productList1){
        List<Product> productList=productDAO.getListProduct();

        boolean isAvailable=false;
        int available=product.getAvailability();


        if (productList1!=null){
            for (Product temp:productList1){
                if(temp.getId()==product.getId()){
                    available=available+temp.getAvailability();
                }
            }
        }

        for(Product temp: productList) {
            if (temp.getId()==product.getId()) {
                if (temp.getAvailability()>=available){
                    isAvailable=true;
                } else{
                    isAvailable=false;
                }
            }
        }
        return isAvailable;
    }

    @Override
    public Double priceSum(List<Product> productList){
        double price=0.00;
        if (productList!=null) {
            for (Product temp : productList) {
                price = price + (temp.getPrice()*temp.getAvailability());
            }
        }
        return price;
    }

    private int randomId(){
        return new Random().nextInt();
    }

    @Override
    public List<Product> orderedList(List<Product> productList){
        List<Product> temp =new ArrayList<>();
        int sum=0;

        boolean add=false;
        if (productList!=null) {
            for (Product product : productList) {
                add = false;
                if (temp.isEmpty()) {
                    temp.add(product);
                    add = true;
                } else {
                    for (int i = 0; i <= temp.size()-1; i++) {
                        if (product.getId() == temp.get(i).getId()) {
                            sum=temp.get(i).getAvailability() + product.getAvailability();
                            temp.get(i).setAvailability(sum);
                            add = true;
                            break;
                        }
                    }
                }
                if (!add) {
                    temp.add(product);
                }
            }
            return temp;
        } else {
            return null;
        }
    }

    @Override
    public List<Product> deleteProductBasket(int id, List<Product> list){

        if (list!=null){
            for(Product product:list){
                if(id==product.getId()){
                    list.remove(product);
                    break;
                }
            }
        }
        return list;
    }

}

