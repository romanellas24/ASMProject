package api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;

@Data
@AllArgsConstructor
@Schema(description = "Body request for create order. It contains dishes infos and deilvery time")
public class CreateOrderDTO {
    @Schema(description = "list of dishes containing id and quantity")
    private DishInOrderDTO[] dishes;
    @Schema(description = "Delivery time. This is a string that must be in format yyyy-MM-dd HH:mm", format = "yyyy-MM-dd HH:mm")
    private String deliveryTime;

    public Integer[] getDishIds(){
        return Arrays.stream(dishes).map(DishInOrderDTO::getId).toArray(Integer[]::new);
    }
}
