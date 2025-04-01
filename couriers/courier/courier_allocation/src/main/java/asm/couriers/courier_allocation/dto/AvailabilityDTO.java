package asm.couriers.courier_allocation.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityDTO {
    private Double price;
    private Integer time;
    private Double distance;
    private Integer vehicleId;

    public void setPrice(Double price){
        this.price = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
