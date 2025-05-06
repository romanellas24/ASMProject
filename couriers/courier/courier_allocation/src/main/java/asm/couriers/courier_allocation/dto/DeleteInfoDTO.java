package asm.couriers.courier_allocation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DeleteInfoDTO {
    private Integer orderId;
    private Boolean deleted;
}
