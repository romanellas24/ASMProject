package asm.couriers.courier_allocation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@Schema(description = "Request payload for checking vehicle availability")
public class RequestAvailDTO {
    @Schema(description = "Restaurant address")
    private AddressDTO restAddr;
    @Schema(description = "Client address")
    private AddressDTO clientAddr;
    @Schema(description = "Expected delivery time in format yyyy-MM-dd HH:mm",format = "yyyy-MM-dd HH:mm", example = "2025-05-26 14:30")
    private String expectedDeliveryTime;
}
