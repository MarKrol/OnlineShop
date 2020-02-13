package pl.camp.it.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.camp.it.dao.IUserDAO;
import pl.camp.it.model.User;
import pl.camp.it.model.UserRole;
import pl.camp.it.services.IUserServices;
import pl.camp.it.session.SessionObject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    IUserServices userServices;
    @Autowired
    IUserDAO userDAO;

    @Resource
    SessionObject sessionObject;

    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public String showUserList(Model model){
        if (sessionObject.isLogged()) {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userObject",userDAO.getUsers());
            return "userList";
        }else{
         return "redirect:/index";
        }
    }

    @RequestMapping(value = "/userList/{id}", method = RequestMethod.GET)
    public String upgradeUserToAdmin(@PathVariable int id, Model model){
        List<User> temp = new ArrayList<>();

        if (sessionObject.isLogged()) {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            for(User user: userDAO.getUsers()){
                if (id==user.getId()){
                    user.setUserRole(UserRole.ADMIN);
                    temp.add(user);
                }else{
                    temp.add(user);
                }
            }
            userDAO.saveUsers(temp);
            return "redirect:/userList";
        }else{
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "userAdmin", method = RequestMethod.GET)
    public String showUserAdmin(Model model){
        if (sessionObject.isLogged()) {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userObject",userDAO.getUsers());
            return "userAdmin";
        }else{
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/userAdmin/{id}", method = RequestMethod.GET)
    public String changeAdminToUser(@PathVariable int id, Model model){
        List<User> temp = new ArrayList<>();

        if (sessionObject.isLogged()) {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            for(User user: userDAO.getUsers()){
                if (id==user.getId()){
                    user.setUserRole(UserRole.USER);
                    temp.add(user);
                }else{
                    temp.add(user);
                }
            }
            userDAO.saveUsers(temp);
            return "redirect:/userAdmin";
        }else{
            return "redirect:/index";
        }
    }

}

