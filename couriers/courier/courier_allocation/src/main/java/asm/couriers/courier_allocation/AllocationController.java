package asm.couriers.courier_allocation;

import asm.couriers.courier_allocation.dto.*;
import asm.couriers.courier_allocation.service.AllocationService;
import asm.couriers.courier_allocation.service.AuthService;
import asm.couriers.courier_allocation.service.MapsService;
import asm.couriers.courier_allocation.utils.Const;
import asm.couriers.courier_allocation.utils.StringToLocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping()
public class AllocationController {
    @Autowired
    private AllocationService allocationService;

    @Autowired
    private MapsService mapsService;

    @Autowired
    private AuthService authService;

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
    @ResponseBody
    OrderInfoDTO allocateVehicle(@RequestBody RequestAllocateDTO request) throws Exception {

        if (request == null || request.getVehicle() == null || request.getTimeMinutes() == null || request.getExpectedDeliveryTime() == null) {
            throw new Exception("Invalid request body");
        }

        if (!StringToLocalDateTime.isStringValid(request.getExpectedDeliveryTime())) {
            throw new Exception("Invalid date format");
        }

        CompanyDTO company = authService.getCompanyFromNameAndHash(request.getCompanyName(), request.getHash());

        return allocationService.allocate_vehicle(request, company);
    }

    @DeleteMapping(produces = "application/json")
    @ResponseBody
    public DeleteInfoDTO deleteOrder(@RequestBody RequestDeleteDTO request) throws Exception {

        if (request == null || request.getOrderId() == null || request.getHash() == null || request.getCompanyName()== null) {
            throw new Exception("Invalid request body");
        }

        CompanyDTO company = authService.getCompanyFromNameAndHash(request.getCompanyName(), request.getHash());

        Boolean deleted = allocationService.delete_order(request.getOrderId(), company);

        DeleteInfoDTO deleteInfoDTO = new DeleteInfoDTO();
        deleteInfoDTO.setOrderId(request.getOrderId());
        deleteInfoDTO.setDeleted(deleted);

        return deleteInfoDTO;
    }


}
