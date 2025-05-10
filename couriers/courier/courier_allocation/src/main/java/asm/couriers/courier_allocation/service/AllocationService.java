package asm.couriers.courier_allocation.service;


import asm.couriers.courier_allocation.dto.*;
import asm.couriers.courier_allocation.exception.DeleteRequestTooLateException;
import asm.couriers.courier_allocation.exception.NotFoundException;
import asm.couriers.courier_allocation.exception.UnauthorizedException;
import asm.couriers.courier_allocation.exception.VehicleNotAvailableException;

import java.time.LocalDateTime;

public interface AllocationService {
    Integer vehicle_available(Integer deliveryTime, LocalDateTime expectedDelivery) throws VehicleNotAvailableException;
    OrderInfoDTO allocate_vehicle(RequestAllocateDTO request, CompanyDTO company) throws Exception;
    Boolean delete_order(Integer orderId, CompanyDTO company) throws DeleteRequestTooLateException, UnauthorizedException, NotFoundException;
    VehicleDTO getVehicleById(Long id);
    VehicleDTO changeStateById(Long id);
}
