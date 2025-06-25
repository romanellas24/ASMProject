package api.dto;

import api.entity.Dish;
import api.entity.DishOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "dish in order. It differs from Dish for quantity.")
public class DishOrderDTO {
    @Schema(description = "dish ordered")
    private DishDTO dish;
    @Schema(description = "quantity of dish ordered", example = "2")
    private Integer quantity;

    public static DishOrderDTO from(DishOrder dish) {
        DishOrderDTO dishDTO = new DishOrderDTO();
        dishDTO.setDish(DishDTO.from(dish.getDish()));
        dishDTO.setQuantity(dish.getMultiplicative());
        return dishDTO;
    }

    public static DishOrderDTO from(DishDTO dish, Integer quantity){
        DishOrderDTO dishDTO = new DishOrderDTO();
        dishDTO.setQuantity(quantity);
        dishDTO.setDish(dish);
        return dishDTO;
    }

    public static DishOrderDTO[] from(Set<DishOrder> dishes) {
        return dishes.stream()
                .map(DishOrderDTO::from)
                .toArray(DishOrderDTO[]::new);
    }
}
