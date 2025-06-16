package asm.couriers.courier_allocation.utils;

import asm.couriers.courier_allocation.dto.VehicleDTO;
import asm.couriers.courier_allocation.entity.Vehicle;

public class VehicleToDtoMapper {
    public static VehicleDTO toDto(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setAvailable(vehicle.getAvailable());
        return dto;
    }
}
