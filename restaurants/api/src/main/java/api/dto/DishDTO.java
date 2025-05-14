package api.dto;

import api.entity.Dish;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class DishDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;

    public static DishDTO from(Dish dish) {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setId(dish.getId());
        dishDTO.setName(dish.getName());
        dishDTO.setDescription(dish.getDescription());
        dishDTO.setPrice(dish.getPrice());
        return dishDTO;
    }

    public static DishDTO[] from(Set<Dish> dishes) {
        return dishes.stream()
                .map(DishDTO::from)
                .toArray(DishDTO[]::new);

//        DishDTO[] dishDTOs = new DishDTO[dish.size()];
//        Dish[] dishes = new Dish[dish.size()];
//        dish.toArray(dishes);
//        for (int i = 0; i < dish.size(); i++) {
//            dishDTOs[i] = from(dishes[i]);
//        }
//        return dishDTOs;
    }

}
