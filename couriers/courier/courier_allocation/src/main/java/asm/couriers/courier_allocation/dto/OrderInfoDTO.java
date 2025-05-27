package asm.couriers.courier_allocation.dto;

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
}