package asm.couriers.courier_tracking;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class TrackingController {

    @GetMapping(value="/{vehicle_id}",produces = "application/json")
    public Integer getState(@PathVariable String vehicle_id) {
        return 0;
    }
}
