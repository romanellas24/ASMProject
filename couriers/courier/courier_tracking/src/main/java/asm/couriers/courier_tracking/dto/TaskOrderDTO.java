package asm.couriers.courier_tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskOrderDTO {
    private String taskId;
    private OrderDTO order;
}
