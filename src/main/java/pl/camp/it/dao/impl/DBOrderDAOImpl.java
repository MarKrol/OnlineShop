package pl.camp.it.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.camp.it.dao.IOrderDAO;
import pl.camp.it.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DBOrderDAOImpl implements IOrderDAO {

    @Autowired
    Connection connection;

    @Override
    public void persistOrder(Order order) {
        try{
            int orderId=0;
            String sql0="INSERT INTO torder (localDate, orderState, userId) VALUE (?,?,?)";
            PreparedStatement ps0 = this.connection.prepareStatement(sql0);
            ps0.setDate(1, Date.valueOf(order.getLocalDate()));
            ps0.setString(2,order.getOrderState().toString());
            ps0.setInt(3,order.getUser().getId());
            ps0.executeUpdate();

            String sql1="SELECT * FROM torder ORDER BY id DESC LIMIT 1";
            PreparedStatement ps1 = this.connection.prepareStatement(sql1);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()){
               orderId= rs.getInt("id");
            }

            String sql2="INSERT INTO tlistorder (orderId, productId, availability) VALUE (?,?,?)";
            PreparedStatement ps2 = this.connection.prepareStatement(sql2);
            for (int i=0; i<=order.getProductList().size()-1; i++){
                ps2.setInt(1,orderId);
                ps2.setInt(2,order.getProductList().get(i).getId());
                ps2.setInt(3,order.getProductList().get(i).getAvailability());
                ps2.executeUpdate();
            }
        }catch(SQLException e){
            System.out.println("Błąd zapisu zamówienia do bazy!");
            e.getStackTrace();
        }

    }

    @Override
    public List<Order> getListOrder() {
        List<Order> orderList = new ArrayList<>();

        try {
            String sql = "SELECT * FROM torder;";
            PreparedStatement ps = this.connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setLocalDate(rs.getDate("localDate").toLocalDate());
                order.setOrderState(OrderState.valueOf(rs.getString("orderState")));

                User user = new User();
                user.setId(rs.getInt("userId"));

                String sql1="SELECT * FROM tuser WHERE id=?;";
                PreparedStatement ps1 = this.connection.prepareStatement(sql1);

                ps1.setInt(1, user.getId());

                ResultSet rs1 =ps1.executeQuery();
                while(rs1.next()) {
                    user.setName(rs1.getString("name"));
                    user.setPass(rs1.getString("pass"));
                    user.setUserRole(UserRole.valueOf(rs1.getString("userRole")));
                }
                order.setUser(user);

                String sql2="SELECT * FROM tlistorder WHERE orderId=?";
                PreparedStatement ps2 = this.connection.prepareStatement(sql2);

                ps2.setInt(1, order.getId());
                ResultSet rs2 = ps2.executeQuery();

                List<Product> productList = new ArrayList<>();
                while (rs2.next()){
                    Product product =new Product();
                    product.setId(rs2.getInt("productId"));
                    product.setAvailability(rs2.getInt("availability"));

                    String sql3 = "SELECT * FROM tproduct WHERE id=?;";
                    PreparedStatement ps3 = this.connection.prepareStatement(sql3);

                    ps3.setInt(1, product.getId());
                    ResultSet rs3 = ps3.executeQuery();

                    while (rs3.next()) {
                        product.setPrice(rs3.getDouble("price"));
                        product.setName(rs3.getString("name"));
                        product.setState(true);

                        productList.add(product);
                    }
                }
                order.setProductList(productList);
                orderList.add(order);
            }
        } catch(SQLException e){
            System.out.println("Błąd odczytu z bazy danych!");
            e.getStackTrace();
        }
        return orderList;
    }

    @Override
    public void saveChangeOrder(List<Order> orderList) {

        try {
            String sql = "UPDATE torder SET orderState=? WHERE id=?";
            PreparedStatement ps = this.connection.prepareStatement(sql);

            ps.setInt(2, orderList.get(0).getId());
            ps.setString(1, orderList.get(0).getOrderState().toString());
            ps.executeUpdate();
        } catch(SQLException e){
            System.out.println("Błąd zapisu do bazy");
            e.getStackTrace();
        }
    }
}
