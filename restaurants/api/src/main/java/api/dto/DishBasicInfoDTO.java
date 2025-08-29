package api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "dish basic info, i.e. dish id and quantity")
public class DishBasicInfoDTO {
    @Schema
    private Integer id;
    @Schema
    private Integer quantity;
}
