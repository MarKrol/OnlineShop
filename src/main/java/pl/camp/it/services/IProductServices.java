package pl.camp.it.services;

import pl.camp.it.model.Product;

import java.util.List;

public interface IProductServices {

    Product completeProduct(Product product);
    Product findProductById(int id);
    void saveChangeToFile1(Product product);
    List<Product> showAvailableProducts(List<Product> productListDB, List<Product> sessionObject);
    Product rememberProducers(Product product);
    boolean isAvailableProduct(Product product, List<Product> productList1);
    Double priceSum(List<Product> productList);
    List<Product> orderedList(List<Product> productList);
    List<Product> deleteProductBasket(int id, List<Product> list);
}
