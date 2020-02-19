package pl.camp.it.dao.impl;

import org.springframework.stereotype.Repository;
import pl.camp.it.dao.IOrderDAO;
import pl.camp.it.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements IOrderDAO {

    private String fileName=".\\src\\main\\resources\\order1.txt";

    @Override
    public void saveChangeOrder(List<Order> orderList) {
        try(BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(fileName))){
            for (Order order: orderList) {
                bufferedWriter.write(order.toString());
                bufferedWriter.newLine();
            }
        }catch(FileNotFoundException e){
            e.getMessage();
        } catch (IOException e){
            e.getMessage();
        }
    }


    @Override
    public void persistOrder(Order order){
        try(BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(fileName,true))){
            bufferedWriter.write(order.toString());
            bufferedWriter.newLine();
        }catch(FileNotFoundException e){
            e.getMessage();
        } catch (IOException e){
            e.getMessage();
        }
    }

    @Override
    public List<Order> getListOrder(){
        List<Order> tempOrder = new ArrayList<>();

        String line;

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))){
            while((line=bufferedReader.readLine())!=null){
                tempOrder.add(convertLineToOrderObject(line));
            }
        } catch (FileNotFoundException e){
            e.getMessage();
        } catch (IOException e){
            e.getMessage();
        }
        return tempOrder;
    }

    private Order convertLineToOrderObject(String line){
        Order order =new Order();
        User user = new User();
        List<Product> products=new ArrayList<>();

        String field1="";
        String field2="";
        int counter=0;

        for (int i=0; i<=line.length()-1;i++){
            if (line.charAt(i)!='['){
                field1=field1+line.charAt(i);
            } else{
                counter=i;
                break;
            }
        }
        for (int i=++counter; i<=line.length()-2;i++){
            if (line.charAt(i)!='['){
                field2=field2+line.charAt(i);
            }
        }

        String[] field3=field1.split("<:@:>");
        order.setId(Integer.parseInt(field3[0]));
        order.setLocalDate(LocalDate.parse(field3[1]));
        order.setOrderState(OrderState.valueOf(field3[2]));

        user.setId(Integer.parseInt(field3[3]));
        user.setName(field3[4]);
        user.setPass(field3[5]);
        user.setUserRole(UserRole.valueOf(field3[6]));

        order.setUser(user);

        String[] field4=field2.split(", ");
        String[] field5;
        for(int i=0; i<=field4.length-1;i++){
            Product product = new Product();
            field5=field4[i].split("<:@:>");

            product.setId(Integer.parseInt(field5[0]));
            product.setName(field5[1]);
            product.setPrice(Double.parseDouble(field5[2]));
            product.setAvailability(Integer.parseInt(field5[3]));
            product.setState(Boolean.parseBoolean(field5[4]));

            products.add(product);
        }

        order.setProductList(products);

        return order;
    }
}
