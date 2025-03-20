package asm.couriers.courier_allocation;

import asm.couriers.courier_allocation.dto.AvailabilityDTO;
import asm.couriers.courier_allocation.dto.RequestAvailDTO;
import asm.couriers.courier_allocation.service.AllocationService;
import asm.couriers.courier_allocation.service.MapsService;
import asm.couriers.courier_allocation.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        /*
        Per controllo disponibilitá dato:
            * indirizzo ristorante
            * indirizzo cliente
            * orario di consegna
        controllare se un veicolo é disponibile

        Idea:
            Calcolare (attraverso qualche api) tempo di consegna (indirizzo cliente - ristorante)
            Effettuare query in cui:
                ritornare veicoli che non sono presenti nella tabella order nei casi in cui
                    * start_delivery_time < orario di consegna < end_delivery_time
                    * orario di consegna - end_delivery_time > tempo di consegna

            In questo modo otterremo i veicoli che sono disponibili in quella fascia oraria.

            (
            Per rendere piú credibli le cose, potremmo considerare uno scarto di 10 minuti in cui il veicolo
            ipoteticamente arriva al ristora,te quindi( tempo di consegna + 10 min)
            In questo caso bisogna aggiungere anche un'altra condizione, ovvero:
                * orario di consegna + 10 minuti < start_delivery_time
            )

     */
        AvailabilityDTO availabilityDTO = mapsService.getInfoDelivery(request.getRestAddr(),request.getClientAddr());
        availabilityDTO.setVehicleId(allocationService.vehicle_available());

        availabilityDTO.setPrice(availabilityDTO.getDistance() * Const.PRICE_PER_KM);

        return availabilityDTO;
    }

    @PutMapping Integer allocateVehicle() {
        /* Given restaurant's address, client's address and time expected for delivery, the system allocates a vehicle. */
        return 2;
    }

}
