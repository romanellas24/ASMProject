package asm.couriers.courier_allocation.dto;

import asm.couriers.courier_allocation.entity.Order;
import asm.couriers.courier_allocation.utils.VehicleToDtoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Information about the allocated order and vehicle")
public class OrderInfoDTO {

    @Schema(description = "Allocated vehicle details")
    private VehicleDTO vehicle;

    @Schema(description = "Order ID associated with the allocation", example = "1023")
    private Integer orderId;

    @Schema(description = "Planned start time for the delivery in format yyyy-MM-dd HH:mm", format = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDeliveryTime;

    @Schema(description = "Planned end time for the delivery in format yyyy-MM-dd HH:mm", format = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDeliveryTime;

    @Schema(description = "address where delivery starts")
    private String fromAddress;

    @Schema(description = "address where delivery ends")
    private String toAddress;


    @Schema(hidden = true)
    public static OrderInfoDTO fromEntity(Order order){
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
        orderInfoDTO.setOrderId(order.getOrderId());
        orderInfoDTO.setVehicle(VehicleToDtoMapper.toDto(order.getVehicle()));
        orderInfoDTO.setStartDeliveryTime(order.getStart_delivery_time());
        orderInfoDTO.setEndDeliveryTime(order.getEnd_delivery_time());
        orderInfoDTO.setFromAddress(order.getFrom_address());
        orderInfoDTO.setToAddress(order.getTo_address());
        return orderInfoDTO;
    }
}