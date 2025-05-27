package asm.couriers.courier_allocation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Represents availability information for a delivery")
public class AvailabilityDTO {

    @Schema(description = "true if vehicle is available. If false, vehicleId will be null")
    private Boolean isVehicleAvailable;

    @Schema(description = "Calculated delivery price", example = "15.50")
    private Double price;
    @Schema(description = "Estimated delivery time in minutes", example = "35")
    private Integer time;
    @Schema(description = "Distance between restaurant and client in kilometers", example = "12.4")
    private Double distance;
    @Schema(description = "Vehicle ID", example = "42")
    private Integer vehicleId;

    public void setPrice(Double price){
        this.price = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
