package pl.camp.it.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.camp.it.dao.IProductDAO;
import pl.camp.it.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DBProductDAOImpl implements IProductDAO {

    @Autowired
    Connection connection;

    @Override
    public void persistProduct(Product product) {

        try {
            String sql = "INSERT INTO tproduct (name, price, availability, state) VALUE (?,?,?,?);";
            PreparedStatement ps = this.connection.prepareStatement(sql);

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getAvailability());
            ps.setBoolean(4, product.isState());

            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println("Błąd zapisu produktu do bazy!");
            e.getStackTrace();
        }

    }

    @Override
    public List<Product> getListProduct() {
        List<Product> productList = new ArrayList<>();

        try{
            String sql="SELECT * FROM tproduct;";
            PreparedStatement ps=this.connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
             while(rs.next()){
                 Product product=new Product();

                 product.setId(rs.getInt("id"));
                 product.setName(rs.getString("name"));
                 product.setPrice(rs.getDouble("price"));
                 product.setAvailability(rs.getInt("availability"));
                 product.setState(rs.getBoolean("state"));

                 productList.add(product);
             }


        }catch (SQLException e){
            System.out.println("Błąd odczytu produktu z bazy!");
            e.getStackTrace();
        }

        return productList;
    }

    @Override
    public void saveChangeProduct(List<Product> productList, int id) {
        Product tempProduct=new Product();

        try {
            String sql = "UPDATE tproduct SET price=?, availability=?, state=? WHERE id=?";
            PreparedStatement ps = this.connection.prepareStatement(sql);

            for (Product product : productList) {
                if (product.getId() == id) {
                    tempProduct = product;
                }
            }

            ps.setInt(4, id);
            ps.setDouble(1, tempProduct.getPrice());
            ps.setInt(2, tempProduct.getAvailability());
            ps.setBoolean(3,tempProduct.isState());
            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println("Błąd zapisu danych");
            e.getStackTrace();
        }

    }
}
