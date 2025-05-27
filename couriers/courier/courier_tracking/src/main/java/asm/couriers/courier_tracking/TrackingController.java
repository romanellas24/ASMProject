package asm.couriers.courier_tracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping()
public class TrackingController {

    @Autowired
    private String wsUrl;

//    @GetMapping(value="/state/{vehicle_id}",produces = "application/json")
//    public Integer getState(@PathVariable String vehicle_id) {
//        return Integer.valueOf(vehicle_id);
//    }

    @GetMapping(value="/notification/{courier_id}")
    public ModelAndView infoDelivery(@PathVariable Integer courier_id) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("courier_id", courier_id);
        modelAndView.addObject("WSurl", wsUrl);
        return modelAndView;

    }
}
