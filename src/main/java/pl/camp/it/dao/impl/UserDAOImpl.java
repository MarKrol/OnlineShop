package pl.camp.it.dao.impl;

import org.springframework.stereotype.Repository;
import pl.camp.it.dao.IUserDAO;
import pl.camp.it.model.User;
import pl.camp.it.model.UserRole;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class UserDAOImpl implements IUserDAO {

    private String fileName = ".\\src\\main\\resources\\users.txt";
    private String getFileNameSuperUser = ".\\src\\main\\resources\\superUser.txt";


    @Override
    public void saveUsers(List<User> userList) {
        try(BufferedWriter bufferedWriter =new BufferedWriter(new FileWriter(fileName))){

            for(User user: userList) {
                if (!user.getUserRole().toString().equals("SUPER_ADMIN")) {
                    bufferedWriter.write(user.toString());
                    bufferedWriter.newLine();
                }
            }
        }catch (FileNotFoundException e){
            e.getMessage();
        }catch (IOException e){
            e.getMessage();
        }
    }


    @Override
    public void persistUser(User user) {
        try(BufferedWriter bufferedWriter =new BufferedWriter(new FileWriter(fileName, true))){
            bufferedWriter.write(user.toString());
            bufferedWriter.newLine();
        }catch (FileNotFoundException e){
            e.getMessage();
        }catch (IOException e){
            e.getMessage();
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> tempUser = new ArrayList<>();

        tempUser.add(getSuperUser());

        String line;

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))){
            while((line=bufferedReader.readLine())!=null){
                tempUser.add(convertLineToUserObject(line));
            }
        } catch (FileNotFoundException e){
            e.getMessage();
        } catch (IOException e){
            e.getMessage();
        }

        return tempUser;
    }

    private User getSuperUser(){
        User superUser=new User();

        String line;

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(getFileNameSuperUser))){
            while((line=bufferedReader.readLine())!=null){
                superUser=(convertLineToUserObject(line));
            }
        } catch (FileNotFoundException e){
            e.getMessage();
        } catch (IOException e){
            e.getMessage();
        }

        return superUser;
    }

    private User convertLineToUserObject(String line){
        String[] fields = line.split("<:@:>");

        User user = new User();

        user.setId(Integer.parseInt(fields[0]));
        user.setName(fields[1]);
        user.setPass(fields[2]);
        user.setUserRole(UserRole.valueOf(fields[3]));

        return user;
    }
}
