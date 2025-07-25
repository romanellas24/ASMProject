package asm.couriers.courier_allocation.utils;

import asm.couriers.courier_allocation.dto.CompanyDTO;
import asm.couriers.courier_allocation.dto.RequestAllocateDTO;
import asm.couriers.courier_allocation.entity.Order;
import asm.couriers.courier_allocation.entity.Vehicle;

import java.time.LocalDateTime;

public class RequestDTOtoOrderMapper {
    public static Order convertToOrder(RequestAllocateDTO requestDTO) {

        LocalDateTime expectedDeliveryTime = StringToLocalDateTime.convertStringToLocalDateTime(requestDTO.getExpectedDeliveryTime());
        LocalDateTime startDeliveryTime = expectedDeliveryTime.minusMinutes(requestDTO.getTimeMinutes());

        Vehicle vehicle = new Vehicle();
        vehicle.setId(requestDTO.getVehicle());

        Order order = new Order();
        order.setEnd_delivery_time(expectedDeliveryTime);
        order.setStart_delivery_time(startDeliveryTime);
        order.setVehicle(vehicle);
        order.setTo_address(requestDTO.getUserAddress());
        order.setFrom_address(requestDTO.getLocalAddress());

        return order;
    }

    public static Order convertToOrder(RequestAllocateDTO requestDTO, CompanyDTO company) {
        Order order = RequestDTOtoOrderMapper.convertToOrder(requestDTO);
        order.setCompany(CompanyToDtoMapper.toEntity(company));

        return order;
    }
}
