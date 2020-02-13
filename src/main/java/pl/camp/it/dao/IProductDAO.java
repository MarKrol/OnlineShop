package pl.camp.it.dao;

import pl.camp.it.model.Product;

import java.util.List;

public interface IProductDAO {
    void persistProduct(Product product);
    List<Product> getListProduct();
}
