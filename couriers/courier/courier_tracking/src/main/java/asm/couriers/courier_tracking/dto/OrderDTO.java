package asm.couriers.courier_tracking.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class OrderDTO {
    private Integer orderId;
    private Integer vehicleId;
    private LocalDateTime startDeliveryTime;
    private LocalDateTime endDeliveryTime;
}
