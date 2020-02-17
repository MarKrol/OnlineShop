package pl.camp.it.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.camp.it.dao.IProductDAO;
import pl.camp.it.model.Product;
import pl.camp.it.model.User;
import pl.camp.it.services.IProductServices;
import pl.camp.it.services.IUserServices;
import pl.camp.it.session.SessionObject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    IUserServices userServices;

    @Resource
    SessionObject sessionObject;

    @Autowired
    IProductServices productServices;

    @Autowired
    IProductDAO productDAO;

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
            sessionObject.setProductList(null);
        }else{
            model.addAttribute("errorMessage", "Zaloguj się, aby się wylogować!!!");
            model.addAttribute("userLogged", "Gość");
        }
        return "logout";
    }

    @RequestMapping(value = "/productsUser/{id}",method = RequestMethod.GET)
    public String goToPageBuyProducts(@PathVariable int id, Model model){
        if (sessionObject.isLogged()&& sessionObject.getUser().getUserRole().toString()=="USER") {
            sessionObject.setProductID(id);
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            return "redirect:/addToCart";
        } else{
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/productsUser", method = RequestMethod.GET)
    public String showAllProducts(Model model){
        String role;
        if (sessionObject.getUser()==null){
            model.addAttribute("userLogged", "Gość");
            role="";
        }else {
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            role=sessionObject.getUser().getUserRole().toString();
        }
        model.addAttribute("listProducts", productServices.showAvailableProducts
                                   (productDAO.getListProduct(), sessionObject.getProductList(), role));
        return "productsUser";
    }

    @RequestMapping(value="/productsUser", method = RequestMethod.POST)
    public String showAllProductsFilter(@RequestParam String filter, Model model){
        String role;
        if (sessionObject.getUser()==null){
            model.addAttribute("userLogged", "Gość");
            role="";
        }else {
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            role=sessionObject.getUser().getUserRole().toString();
        }

        List<Product> filters=userServices.filtrProducts(productServices.showAvailableProducts
                (productDAO.getListProduct(), sessionObject.getProductList(), role), filter);

        model.addAttribute("listProducts", filters);
        return "productsUser";
    }

    @RequestMapping(value = "/addToCart",method = RequestMethod.GET)
    public String addToCart(Model model){
        if (sessionObject.isLogged()&& sessionObject.getUser().getUserRole().toString()=="USER") {
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            Product tempProduct = productServices.findProductById(sessionObject.getProductID());
            tempProduct.setAvailability(1);

            model.addAttribute("productById", tempProduct);
            return "addToCart";
        }else{
            return "redirect:/index";
        }
    }

    @RequestMapping(value ="/addToCart", method = RequestMethod.POST)
    public String insertToCart(@ModelAttribute Product product, Model model){
        if (sessionObject.isLogged()&& sessionObject.getUser().getUserRole().toString()=="USER") {
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            if (productServices.isAvailableProduct(product, sessionObject.getProductList())) {
                if (sessionObject.getProductList() == null) {
                    List<Product> productList = new ArrayList<>();
                    productList.add(productServices.rememberProducers(product));
                    sessionObject.setProductList(productList);
                } else {
                    List<Product> productList = sessionObject.getProductList();
                    productList.add(productServices.rememberProducers(product));
                    sessionObject.setProductList(productList);
                }

            } else {
                model.addAttribute("isAvailable", "false");
                return "productsUser";
            }

            return "redirect:/basket";
        }else{
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/basket", method = RequestMethod.GET)
    public String showbasket(Model model){
        if (sessionObject.isLogged()&& sessionObject.getUser().getUserRole().toString()=="USER") {
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            sessionObject.setProductList(productServices.orderedList(sessionObject.getProductList()));
            model.addAttribute("price", productServices.priceSum(sessionObject.getProductList()));
            model.addAttribute("listProducts", sessionObject.getProductList());
            return "basket";
        }else{
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/basket/{id}", method = RequestMethod.GET)
    public String deleteProduct(@PathVariable int id, Model model){
        if (sessionObject.isLogged()&& sessionObject.getUser().getUserRole().toString()=="USER") {
            model.addAttribute("userRole",sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("listProducs", productServices.deleteProductBasket(id, sessionObject.getProductList()));
            return "redirect:/basket";
        }else{
            return "redirect:/index";
        }
    }
}
