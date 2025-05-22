package api.dto;

import api.entity.Dish;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@Schema(description = "dish object containing id, name description and price")
public class DishDTO {
    @Schema(description = "dish id", example = "1")
    private Integer id;

    @Schema(description = "dish name", example = "carbonara")
    private String name;

    @Schema(description = "dish description", example = "Jowls, Egg yolks, Pecorino Romano PDO,Black pepper")
    private String description;

    @Schema(description = "dish price", example = "7.50", type = "number", format = "double")
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
    }

}
