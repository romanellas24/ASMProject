package asm.couriers.courier_tracking.utils;


import asm.couriers.courier_tracking.dto.OrderDTO;
import asm.couriers.courier_tracking.entity.Order;

public class OrderToDtoMapper {
    public static OrderDTO toDto(Order order){
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrder_id());
        dto.setVehicleId(order.getVehicle().getId());
        dto.setEndDeliveryTime(order.getEndDeliveryTime());
        dto.setStartDeliveryTime(order.getStartDeliveryTime());
        return dto;
    }
}
