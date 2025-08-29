package asm.couriers.courier_allocation.service;

import asm.couriers.courier_allocation.dto.AvailabilityDTO;


public interface MapsService {
    // si puó aggiungere anche l'orario di arrivo desiderato
    AvailabilityDTO getInfoDelivery(String restAddr, String clientAddr) throws Exception;
}
