package asm.couriers.courier_allocation.service;


import asm.couriers.courier_allocation.dto.RequestAllocateDTO;
import asm.couriers.courier_allocation.dto.VehicleDTO;
import asm.couriers.courier_allocation.exception.VehicleNotAvailableException;

import java.time.LocalDateTime;

public interface AllocationService {
    Integer vehicle_available(Integer deliveryTime, LocalDateTime expectedDelivery) throws VehicleNotAvailableException;
    VehicleDTO allocate_vehicle(RequestAllocateDTO request) throws Exception;
    VehicleDTO getVehicleById(Long id);
    VehicleDTO changeStateById(Long id);
}
