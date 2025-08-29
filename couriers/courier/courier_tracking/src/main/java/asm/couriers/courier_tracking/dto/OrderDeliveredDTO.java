package asm.couriers.courier_tracking.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrderDeliveredDTO {
    private Integer orderId;
    private Integer courierId;
    private Boolean delivered;
    private String taskId;
}
