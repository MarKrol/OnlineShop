package pl.camp.it.services;

import pl.camp.it.model.User;

public interface IUserServices {
    boolean userInDataBaseByName(String name);
    boolean matchingPasswords(String pass1, String pass2);
    void addUserInDataBase(User user);
    boolean login(User user);
    String errorMessage();
    User getOnlyUser(User user);
}
