package pl.camp.it.dao.impl;

import org.springframework.stereotype.Repository;
import pl.camp.it.dao.IProductDAO;
import pl.camp.it.model.Product;
import pl.camp.it.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class IProductDAOImpl implements IProductDAO {

    private String fileName=".\\src\\main\\resources\\products1.txt";

    @Override
    public void saveChangeProduct(List<Product> productList, int id) {
        try(BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(fileName))){
            for (Product product: productList) {
                bufferedWriter.write(product.toString());
                bufferedWriter.newLine();
            }
        }catch(FileNotFoundException e){
            e.getMessage();
        } catch (IOException e){
            e.getMessage();
        }
    }

    @Override
    public void persistProduct(Product product) {
        try(BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(fileName,true))){
            bufferedWriter.write(product.toString());
            bufferedWriter.newLine();
        }catch(FileNotFoundException e){
            e.getMessage();
        } catch (IOException e){
            e.getMessage();
        }
    }

    @Override
    public List<Product> getListProduct() {
        List<Product> productList = new ArrayList<>();

        String line;

        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(fileName))){
            while ((line=bufferedReader.readLine())!=null){
                productList.add(convertLineToProduct(line));
            }
        }catch (FileNotFoundException e){
            e.getMessage();
        } catch (IOException e){
            e.getMessage();
        }
        return productList;
    }

    private Product convertLineToProduct(String line){
        String[] field=line.split("<:@:>");

        Product tempProduct=new Product();

        tempProduct.setId(Integer.parseInt(field[0]));
        tempProduct.setName(field[1]);
        tempProduct.setPrice(Double.parseDouble(field[2]));
        tempProduct.setAvailability(Integer.parseInt(field[3]));
        tempProduct.setState(Boolean.parseBoolean(field[4]));

        return tempProduct;
    }
}
