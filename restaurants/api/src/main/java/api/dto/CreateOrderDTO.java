package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateOrderDTO {
    private Integer[] dishIds;
    private String deliveryTime;
}
