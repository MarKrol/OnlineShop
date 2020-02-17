package pl.camp.it.controllers;


import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.camp.it.dao.IOrderDAO;
import pl.camp.it.dao.IProductDAO;
import pl.camp.it.dao.IUserDAO;
import pl.camp.it.model.*;
import pl.camp.it.services.IOrderServices;
import pl.camp.it.services.IProductServices;
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
    @Autowired
    IProductDAO productDAO;
    @Autowired
    IProductServices productServices;
    @Autowired
    IOrderServices orderServices;
    @Autowired
    IOrderDAO orderDAO;

    @Resource
    SessionObject sessionObject;

    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public String showUserList(Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                                                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
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

        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
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
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
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

        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
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

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String buttonProducts(Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            return "products";
        }else{
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST, params = "addProducts=Dodaj produkt")
    public String clicAddProduct(Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("addProduct", "true");
            model.addAttribute("product", new Product());
            return "products";
        }else{
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/products",method = RequestMethod.POST,
                                                                params = "addProductToDataBase=Dodaj produkt do bazy")
    public String formAddProduct(@ModelAttribute Product product, Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            productDAO.persistProduct(productServices.completeProduct(product));
            return "redirect:/products";
        } else{
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/products",method = RequestMethod.POST,
                                                            params = "showAllProducts=Wyświetl wszysztkie produkty")
    public String showAllProducts(Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("showAllProducts", "true");
            model.addAttribute("listProducts", productDAO.getListProduct());
            return "products";
        }else{
            return "redirect:/login";
        }
    }

    @RequestMapping(value="/products", method = RequestMethod.POST, params = "filter=Znajdź produkty")
    public String showAllProductsFilter(Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            return "redirect:/findAdmin";
        }else{
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "findAdmin", method = RequestMethod.GET)
    public String findAdmin(Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            List<Product> filters = userServices.filtrProducts(productServices.showAvailableProducts
                    (productDAO.getListProduct(), new ArrayList<Product>(),
                                                        sessionObject.getUser().getUserRole().toString()),"");

            model.addAttribute("listProducts", filters);

            return "findAdmin";
        } else{
            return "redirect:/login";
        }
    }

    @RequestMapping(value="/findAdmin", method = RequestMethod.POST)
    public String showAllProductsFilters(@RequestParam (name = "filters") String filter, Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());


            List<Product> filters = userServices.filtrProducts(productServices.showAvailableProducts
                    (productDAO.getListProduct(), new ArrayList<Product>(),
                                                        sessionObject.getUser().getUserRole().toString()), filter);

            model.addAttribute("listProducts", filters);
            return "findAdmin";
        } else{
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/products/{id}",method = RequestMethod.GET)
    public String showDetailsProduct(@PathVariable int id, Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            sessionObject.setProductID(id);
            return "redirect:/detailsProduct";
        }else{
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/detailsProduct",method = RequestMethod.GET)
    public String detailsProduct(Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("productById", productServices.findProductById(sessionObject.getProductID()));
            return "detailsProduct";
        }else{
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/products/{id}/{name}",method = RequestMethod.GET)
    public String showEditProduct(@PathVariable int id, @PathVariable String name, Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            sessionObject.setProductID(id);
            return "redirect:/editProduct";
        }
        else{
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/editProduct",method = RequestMethod.GET)
    public String editProduct(Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("productById", productServices.findProductById(sessionObject.getProductID()));
            return "editProduct";
        }else {
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/editProduct",method = RequestMethod.POST,
                                                            params="addProductToDataBase=Zaktualizuj produkt w bazie")
    public String upgradeProduct(@ModelAttribute Product product, Model model){
        if (sessionObject.isLogged() && sessionObject.getUser().getUserRole().toString()=="ADMIN" ||
                sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN") {
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            productServices.saveChangeToFile1(product);
            return "products";
        }else {
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "historyAdmin",method = RequestMethod.GET)
    public String historyAdmin(Model model){
        if (sessionObject.isLogged()&& (sessionObject.getUser().getUserRole().toString()=="ADMIN"
                || sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN")) {
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("order", orderDAO.getListOrder());
            return "historyAdmin";
        }else{
            return "redirect:/login";
        }
    }


    @RequestMapping(value ="/historyAdmin", method = RequestMethod.POST, params = "Admin=Wyświetl zamówenie")
    public String showAdmin(@RequestParam String choose, Model model){
        List<Order> tempOrder=new ArrayList<>();

        if (sessionObject.isLogged()&& (sessionObject.getUser().getUserRole().toString()=="ADMIN"
                || sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN")) {
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            if (choose!="") {
                List<Order> order1 = orderDAO.getListOrder();
                for (Order temp : order1) {
                     if (Integer.parseInt(choose) == temp.getId()) {
                         tempOrder.add(temp);
                    }
                }
                model.addAttribute("price", orderServices.priceReturnOrderUser(tempOrder));
                model.addAttribute("tempOrder", tempOrder);
            }
            model.addAttribute("order", orderDAO.getListOrder());
            return "historyAdmin";
        }else{
            return "redirect:/login";
        }
    }

    @RequestMapping(value="/historyAdmin/{id}", method = RequestMethod.GET)
    public String changeStatus(@PathVariable int id, Model model){
        if (sessionObject.isLogged()&& (sessionObject.getUser().getUserRole().toString()=="ADMIN"
                || sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN")) {
            orderServices.setReturnId(id);
            return "redirect:/changeStatus";
        }else{
            return "redirect:/login";
        }
    }

    @RequestMapping(value="/changeStatus", method = RequestMethod.GET)
    public String changeStatus(Model model){

        if (sessionObject.isLogged()&& (sessionObject.getUser().getUserRole().toString()=="ADMIN"
                || sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN")) {
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());

            List<Order> orders = orderDAO.getListOrder();
            Order tempOrder = new Order();
            for (Order order : orders) {
                if (orderServices.getReturnId() == order.getId()) {
                    tempOrder = order;
                    break;
                }
            }

            orders.clear();
            orders.add(tempOrder);

            model.addAttribute("price", orderServices.priceReturnOrderUser(orders));
            model.addAttribute("tempOrder", tempOrder);
            model.addAttribute("order", orderDAO.getListOrder());

            return "changeStatus";
        }else{
            return "redirect:/login";
        }
    }


    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public String changeStatusOrder(@RequestParam("status") String value, Model model){

        if (sessionObject.isLogged()&& (sessionObject.getUser().getUserRole().toString()=="ADMIN"
                || sessionObject.getUser().getUserRole().toString()=="SUPER_ADMIN")) {
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());

            List<Order> orders = orderDAO.getListOrder();
            Order tempOrder = new Order();
            for (Order order : orders) {
                if (orderServices.getReturnId() == order.getId()) {
                    order.setOrderState(OrderState.valueOf(value));
                    tempOrder = order;
                    break;
                }
            }
            orderDAO.saveChangeOrder(orders);
            model.addAttribute("tempOrder", tempOrder);
            model.addAttribute("order", orderDAO.getListOrder());
            return "redirect:/historyAdmin";
        }else{
            return "redirect:/login";
        }
    }

}

