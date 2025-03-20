package asm.couriers.courier_allocation.dto;

import lombok.*;

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

}
