package asm.couriers.courier_allocation;

import asm.couriers.courier_allocation.dto.AvailabilityDTO;
import asm.couriers.courier_allocation.dto.RequestAllocateDTO;
import asm.couriers.courier_allocation.dto.RequestAvailDTO;
import asm.couriers.courier_allocation.service.AllocationService;
import asm.couriers.courier_allocation.service.MapsService;
import asm.couriers.courier_allocation.utils.Const;
import asm.couriers.courier_allocation.utils.StringToLocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/courier/availability")
public class AllocationController {
    @Autowired
    private AllocationService allocationService;

    @Autowired
    MapsService mapsService;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public AvailabilityDTO checkAvailability(RequestAvailDTO request) throws Exception {

        LocalDateTime dateTimeDelivery = StringToLocalDateTime.convertStringToLocalDateTime(request.getExpectedDeliveryTime());

        /*
        Per controllo disponibilitá dato:
            * indirizzo ristorante
            * indirizzo cliente
            * orario di consegna
        controllare se un veicolo é disponibile
        */
        AvailabilityDTO availabilityDTO = mapsService.getInfoDelivery(request.getRestAddr(),request.getClientAddr());
        Integer vehicleId = allocationService.vehicle_available(availabilityDTO.getTime(), dateTimeDelivery);
        availabilityDTO.setVehicleId(vehicleId);

        availabilityDTO.setPrice(availabilityDTO.getDistance() * Const.PRICE_PER_KM);

        return availabilityDTO;
    }

    @PutMapping(produces = "application/json")
    Integer allocateVehicle(@RequestBody RequestAllocateDTO request) throws Exception {
        allocationService.allocate_vehicle(request);
        return 2;

    }

}
