package pl.camp.it.services.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.camp.it.dao.IUserDAO;
import pl.camp.it.model.Product;
import pl.camp.it.model.User;
import pl.camp.it.model.UserRole;
import pl.camp.it.services.IUserServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserServicesImpl implements IUserServices {

    @Autowired
    IUserDAO userDAO;

    private String errorMessage;

    @Override
    public boolean userInDataBaseByName(String name) {
        List<User> userList = userDAO.getUsers();

        boolean userInDataBase = false;

        for (User tempUser: userList){
            if (tempUser.getName().equals(name)){
                errorMessage="Użytkownik o podanym loginie znajduje się w bazie. Wprowadź inny login!!!";
                userInDataBase=true;
                return userInDataBase;
            }
        }
        return userInDataBase;
    }

    @Override
    public boolean matchingPasswords(String pass1, String pass2) {
        if (pass1.equals(pass2)){
            return true;
        } else{
            errorMessage="Wprowadzono niepoprawne hasła. Spróbuj jeszcze raz!!!";
            return false;
        }
    }

    @Override
    public void addUserInDataBase(User user) {
        String pass=DigestUtils.md5Hex(user.getPass());
        //user.setId(userID());
        user.setPass(pass);
        user.setUserRole(UserRole.USER);
        userDAO.persistUser(user);
    }

    @Override
    public boolean login(User user) {
        List<User> userList = userDAO.getUsers();

        boolean log = false;

        for (User tempUser: userList){
            if (user.getName().equals(tempUser.getName()) &&
                                                    tempUser.getPass().equals(DigestUtils.md5Hex(user.getPass()))){

                log = true;
                break;
            } else{
                errorMessage="Wprowadzono niepoprawny login lub hasło. Spróbuj jeszcze raz!!!";
            }
        }
        return log;
    }

    //private int userID(){
    //    return new Random().nextInt();
    //}

    public User getOnlyUser(User user){
        List<User> userList = userDAO.getUsers();
        User user1=new User();

        for (User tempUsers: userList){
            if (tempUsers.getName().equals(user.getName())){
                user1=tempUsers;
                break;
            }
        }
        return user1;
    }

    @Override
    public String errorMessage(){
        return this.errorMessage;
    }

    @Override
    public List<Product> filtrProducts(List<Product> list, String filter){
        List<Product> tempProducts=new ArrayList<>();
        for(Product product: list){
            if (product.getName().contains(filter)){
                tempProducts.add(product);
            }
        }
        return tempProducts;
    }

}
