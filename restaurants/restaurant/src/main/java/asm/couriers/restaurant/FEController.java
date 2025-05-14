package asm.couriers.restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller()
public class FEController {

    @Value("${be-url}")
    private String beUrl;

    @GetMapping("")
    public ModelAndView homePage(){
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("beUrl", beUrl);
        return modelAndView;
    }

    @GetMapping("/orders")
    public ModelAndView ordersPage(){
        ModelAndView modelAndView = new ModelAndView("orders");
        modelAndView.addObject("beUrl", beUrl);
        return modelAndView;
    }
}
