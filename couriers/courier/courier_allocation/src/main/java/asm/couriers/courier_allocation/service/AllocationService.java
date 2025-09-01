package asm.couriers.courier_allocation.service;


import asm.couriers.courier_allocation.dto.*;
import asm.couriers.courier_allocation.exception.*;

import java.time.LocalDateTime;

public interface AllocationService {
    Integer vehicle_available(Integer deliveryTime, LocalDateTime expectedDelivery) throws VehicleNotAvailableException;
    Boolean isVehicleAvailable(Integer id, Integer deliveryTime, LocalDateTime expectedDelivery) throws BadData;
    OrderInfoDTO allocate_vehicle(RequestAllocateDTO request, CompanyDTO company) throws Exception;

    Boolean delete_order(Integer orderId) throws  NotFoundException;

    OrderInfoDTO getOrder(Integer id) throws NotFoundException;
    OrderInfoDTO getOrder(Integer id, CompanyDTO company) throws NotFoundException;

    CompanyDTO getCompanyFromName(String companyName) throws NotFoundException;

    Boolean isOrderCompanyValid(Integer orderId, CompanyDTO company);
}
