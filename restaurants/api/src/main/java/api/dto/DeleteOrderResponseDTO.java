package api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "response to delete order")
public class DeleteOrderResponseDTO {
    @Schema(description = "order id")
    private Integer orderId;
    @Schema(description = "true if order is deleted, false otherwise (no error)")
    private Boolean deleted;
}
