package api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Body request for create order. It contains dishes infos and delivery time")
public class CreateOrderDTO {
    @Schema(description = "list of dishes containing id and quantity")
    private DishBasicInfoDTO[] dishes;
    @Schema(description = "Delivery time. This is a string that must be in format yyyy-MM-dd HH:mm", format = "yyyy-MM-dd HH:mm")
    private String deliveryTime;
    @Schema(description = "id in company name", example = "10")
    private Integer id;
    @Schema(description = "company name for request", example = "acmeat")
    private String companyName;


    @Schema(hidden = true)
    @JsonIgnore
    public Integer[] getDishIds(){
        return Arrays.stream(dishes).map(DishBasicInfoDTO::getId).toArray(Integer[]::new);
    }
}
