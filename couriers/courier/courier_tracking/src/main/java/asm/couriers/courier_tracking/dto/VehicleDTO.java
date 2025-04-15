package asm.couriers.courier_tracking.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class VehicleDTO {
    private Integer id;
    private Boolean available;
}