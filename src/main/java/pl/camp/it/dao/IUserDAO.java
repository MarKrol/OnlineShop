package pl.camp.it.dao;

import pl.camp.it.model.User;

import java.util.List;

public interface IUserDAO {
    void persistUser(User user);
    List<User> getUsers();
    void saveUsers(List<User> userList);
}
