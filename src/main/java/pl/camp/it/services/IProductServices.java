package pl.camp.it.services;

import pl.camp.it.model.Product;

public interface IProductServices {

    Product completeProduct(Product product);
    Product findProductById(int id);
    void saveChangeToFile1(Product product);
}
