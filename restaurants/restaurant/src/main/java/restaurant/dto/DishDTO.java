package restaurant.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;

}
