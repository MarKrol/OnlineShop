package pl.camp.it.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.camp.it.dao.IUserDAO;
import pl.camp.it.model.User;
import pl.camp.it.model.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DBUserDAOImpl implements IUserDAO {

    @Autowired
    Connection connection;

    @Override
    public void persistUser(User user) {
        try {
            String sql;
            PreparedStatement ps;

            sql = "INSERT INTO tuser (name, pass, userRole) VALUE(?,?,?);";
            ps = this.connection.prepareStatement(sql);

            ps.setString(1, user.getName());
            ps.setString(2, user.getPass());
            ps.setString(3, user.getUserRole().toString());
            System.out.println();
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println("Błąd dodania użytkownika do bazy!");
            e.getStackTrace();
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        try {
            String sql;
            PreparedStatement ps;

            sql = "SELECT * FROM tuser;";
            ps = this.connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPass(rs.getString("pass"));
                user.setUserRole(UserRole.valueOf(rs.getString("userRole")));
                userList.add(user);
            }
        } catch (SQLException e){
            System.out.println("Błą odczytu danych!");
            e.getStackTrace();
        }

        return userList;
    }

    @Override
    public void saveUsers(List<User> userList, int id) {

        try {
            String sql = "UPDATE tuser SET userRole=? WHERE id=?";
            PreparedStatement ps = this.connection.prepareStatement(sql);

            User user = new User();
            for (User tempuser : userList) {
                if (id == tempuser.getId()) {
                    user = tempuser;
                }
            }

            ps.setInt(2, id);
            ps.setString(1, user.getUserRole().toString());
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println("Błąd zapisu do bazy !!!");
            e.getStackTrace();
        }
    }
}
