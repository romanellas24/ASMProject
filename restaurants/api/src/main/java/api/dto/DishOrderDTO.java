package api.dto;

import api.entity.Dish;
import api.entity.DishOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishOrderDTO {
    private DishDTO dish;
    private Integer multiplicative;

    public static DishOrderDTO from(DishOrder dish) {
        DishOrderDTO dishDTO = new DishOrderDTO();
        dishDTO.setDish(DishDTO.from(dish.getDish()));
        dishDTO.setMultiplicative(dish.getMultiplicative());
        return dishDTO;
    }

    public static DishOrderDTO[] from(Set<DishOrder> dishes) {
        return dishes.stream()
                .map(DishOrderDTO::from)
                .toArray(DishOrderDTO[]::new);
    }
}
