package restaurant;

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

    @GetMapping("/waitingorders")
    public ModelAndView wairingOrdersPage(){
        ModelAndView modelAndView = new ModelAndView("waitingOrders");
        modelAndView.addObject("beUrl", beUrl);
        return modelAndView;
    }

    @GetMapping("/menu")
    public ModelAndView menuPage(){
        ModelAndView modelAndView = new ModelAndView("menu");
        modelAndView.addObject("beUrl", beUrl);
        return modelAndView;
    }

    @GetMapping("orders")
    public ModelAndView orders(){
        ModelAndView modelAndView = new ModelAndView("orders");
        modelAndView.addObject("beUrl", beUrl);
        return modelAndView;
    }
}
