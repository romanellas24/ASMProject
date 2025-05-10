package asm.couriers.courier_allocation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoDTO {
    private VehicleDTO vehicle;
    private Integer orderId;
    private LocalDateTime startDeliveryTime;
    private LocalDateTime endDeliveryTime;
}
