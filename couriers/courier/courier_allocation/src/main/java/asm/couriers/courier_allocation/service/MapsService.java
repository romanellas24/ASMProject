package asm.couriers.courier_allocation.service;

import asm.couriers.courier_allocation.dto.AddressDTO;
import asm.couriers.courier_allocation.dto.AvailabilityDTO;
import org.springframework.stereotype.Service;


public interface MapsService {
    // si puó aggiungere anche l'orario di arrivo desiderato
    public AvailabilityDTO getInfoDelivery(AddressDTO restAddr, AddressDTO clientAddr) throws Exception;
}
