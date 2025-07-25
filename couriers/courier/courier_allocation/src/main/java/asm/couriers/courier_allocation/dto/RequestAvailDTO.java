package asm.couriers.courier_allocation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@Schema(description = "Request payload for checking vehicle availability")
public class RequestAvailDTO {
    @Schema(description = "Restaurant address, in human readable format.", example = "Via Gaeatano Scardocchia, 8H, 86100 Campobasso CB")
    private String localAddress;
    @Schema(description = "Client address", example = "Via Saraceni, 157, 86012, Cercemaggiore CB")
    private String userAddress;
    @Schema(description = "Expected delivery time in format yyyy-MM-dd HH:mm",format = "yyyy-MM-dd HH:mm", example = "2025-05-26 14:30")
    private String deliveryTime;
}
