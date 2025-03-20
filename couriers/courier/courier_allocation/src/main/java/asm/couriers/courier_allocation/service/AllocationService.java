package asm.couriers.courier_allocation.service;


import asm.couriers.courier_allocation.dto.VehicleDTO;
import asm.couriers.courier_allocation.exception.VehicleNotAvailableException;

public interface AllocationService {
    public Integer vehicle_available() throws VehicleNotAvailableException;
    public VehicleDTO getVehicleById(Long id);
    public VehicleDTO changeStateById(Long id);
}
