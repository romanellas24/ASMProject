package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;

@Data
@AllArgsConstructor
public class CreateOrderDTO {
    private DishInOrderDTO[] dishes;
    private String deliveryTime;

    public Integer[] getDishIds(){
        return Arrays.stream(dishes).map(DishInOrderDTO::getId).toArray(Integer[]::new);
    }
}
