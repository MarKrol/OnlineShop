package pl.camp.it.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.camp.it.dao.IProductDAO;
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

    private int randomId(){
        return new Random().nextInt();
    }
}

