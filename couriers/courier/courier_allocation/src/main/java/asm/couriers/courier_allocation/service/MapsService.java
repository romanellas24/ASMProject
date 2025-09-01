package asm.couriers.courier_allocation.service;

import asm.couriers.courier_allocation.dto.AvailabilityDTO;


public interface MapsService {
    AvailabilityDTO getInfoDelivery(String restAddr, String clientAddr) throws Exception;
}
