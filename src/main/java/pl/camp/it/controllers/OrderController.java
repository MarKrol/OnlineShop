package pl.camp.it.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.camp.it.dao.IOrderDAO;
import pl.camp.it.model.Order;
import pl.camp.it.services.IOrderServices;
import pl.camp.it.session.SessionObject;

import javax.annotation.Resource;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    IOrderServices orderServices;
    @Autowired
    IOrderDAO orderDAO;

    @Resource
    SessionObject sessionObject;

    @RequestMapping(value = "basket", method = RequestMethod.POST)
    public String order(Model model){
        if (sessionObject.isLogged()&& sessionObject.getUser().getUserRole().toString()=="USER") {
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            if (orderServices.upgradeProductDataBase(sessionObject.getProductList())) {
                orderServices.addOrderToFile(sessionObject.getUser(), sessionObject.getProductList());
                orderServices.saveUpgradeProductDataBase(sessionObject.getProductList());
                sessionObject.setProductList(null);
                return "redirect:/productsUser";
            } else {
                return "basket";
            }
        } else{
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "historyOrder",method = RequestMethod.GET)
    public String historyOrder(Model model){


        if (sessionObject.isLogged()&& sessionObject.getUser().getUserRole().toString()=="USER") {
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            model.addAttribute("order",
                            orderServices.returnOrderUser(sessionObject.getUser().getId(), orderDAO.getListOrder()));
            return "historyOrder";
        }else{
            return "redirect:/login";
        }
    }

    @RequestMapping(value="/historyOrder", method = RequestMethod.POST)
    public String showOrder(@RequestParam  String choose, Model model){
        List<Order> tempOrder=new ArrayList<>();

        if (sessionObject.isLogged()&& sessionObject.getUser().getUserRole().toString()=="USER") {
            model.addAttribute("userRole", sessionObject.getUser().getUserRole().toString());
            model.addAttribute("userLogged", sessionObject.getUser().getName());
            List<Order> order1 = orderServices.returnOrderUser(sessionObject.getUser().getId(), orderDAO.getListOrder());
            for(Order temp: order1){
                if(Integer.parseInt(choose)==temp.getId()){
                    tempOrder.add(temp);
                }
            }
            model.addAttribute("price", orderServices.priceReturnOrderUser(tempOrder));
            model.addAttribute("tempOrder",tempOrder);
            model.addAttribute("order",
                    orderServices.returnOrderUser(sessionObject.getUser().getId(), orderDAO.getListOrder()));

            return "historyOrder";
        }else{
            return "redirect:/login";
        }
    }
}
