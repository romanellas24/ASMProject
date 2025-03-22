package asm.couriers.courier_allocation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RequestAllocateDTO {
    private Integer vehicle;
    private Integer timeMinutes;
    private String expectedDeliveryTime;
}
