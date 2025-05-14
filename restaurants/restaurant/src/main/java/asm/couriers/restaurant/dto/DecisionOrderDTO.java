package asm.couriers.restaurant.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DecisionOrderDTO implements Serializable {
    private String correlationId;
    private Boolean accepted;
}
