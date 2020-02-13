package pl.camp.it.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.camp.it.model.User;
import pl.camp.it.services.IUserServices;
import pl.camp.it.session.SessionObject;

import javax.annotation.Resource;

@Controller
public class UserController {

    @Autowired
    IUserServices userServices;

    @Resource
    SessionObject sessionObject;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model){
        if (sessionObject.isLogged()) {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
        } else{
            model.addAttribute("userLogged", "Gość");
        }
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model){
        if (sessionObject.isLogged()) {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
        } else{
            model.addAttribute("userLogged", "Gość");
        }

        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String back(Model model){
        if (sessionObject.isLogged()) {
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
        } else{
            model.addAttribute("userLogged", "Gość");
        }
        return "index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model){
        if (sessionObject.isLogged()) {
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
        } else{
            model.addAttribute("userLogged", "Gość");
        }
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String logIn(@ModelAttribute User user, Model model){
        if (!sessionObject.isLogged()) {
            if (userServices.login(user)) {
                sessionObject.setLogged(true);
                sessionObject.setUser(userServices.getOnlyUser(user));
                return "redirect:/index";
            } else {
                if (sessionObject.getUser()==null) {
                    model.addAttribute("userLogged", "Gość");
                } else {
                    model.addAttribute("userLogged", sessionObject.getUser().getName());
                }
                model.addAttribute("errorMessage", userServices.errorMessage());
            }
        } else {
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("errorMessage", "Jesteś już zalogowany. " +
                                                            "Wyloguj się, aby zalogować sie na innego użytkownika!!!");
        }
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerIn(@ModelAttribute User user, @RequestParam String pass2, Model model){
        if(!sessionObject.isLogged()) {
            model.addAttribute("userLogged", "Gość");
            if (!userServices.userInDataBaseByName(user.getName()) &&
                    userServices.matchingPasswords(user.getPass(), pass2)) {
                userServices.addUserInDataBase(user);
                return "redirect:/login";
            } else {
                if (sessionObject.getUser()==null) {
                    model.addAttribute("userLogged", "Gość");
                } else {
                    model.addAttribute("userLogged", sessionObject.getUser().getName());
                }
                model.addAttribute("errorMessage", userServices.errorMessage());
            }
        }else{
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("errorMessage","Wyloguj się, aby się zarejestrować!!!");
        }
        return "register";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model){
        if (sessionObject.isLogged()){
            model.addAttribute("userLogged", "Gość");
            model.addAttribute("errorMessage","Zostałeś wylogowany. Zaloguj się ponownie!!!");
            sessionObject.setLogged(false);
            sessionObject.setUser(null);
        }else{
            model.addAttribute("errorMessage", "Zaloguj się, aby się wylogować!!!");
            model.addAttribute("userLogged", "Gość");
        }
        return "logout";
    }
}
