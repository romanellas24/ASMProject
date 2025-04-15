package asm.couriers.courier_tracking;

import asm.couriers.courier_tracking.dao.OrdersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping()
public class TrackingController {

    @Autowired
    private OrdersDAO ordersDAO;

    @GetMapping(value = "")
    public String test() {
        return "pippo coca";
    }

    @GetMapping(value="/state/{vehicle_id}",produces = "application/json")
    public Integer getState(@PathVariable String vehicle_id) {
        //new CheckOrderTask(ordersDAO,Integer.valueOf(vehicle_id)).run();
        return Integer.valueOf(vehicle_id);
    }

    @GetMapping(value="/notification/{courier_id}")
    public ModelAndView infoDelivery(@PathVariable Integer courier_id) {
        ModelAndView modelAndView = new ModelAndView("index");
        // add all html template objects
        modelAndView.addObject("courier_id", courier_id);
        modelAndView.addObject("WSurl", "localhost:8080/notification");
        return modelAndView;

    }
}
