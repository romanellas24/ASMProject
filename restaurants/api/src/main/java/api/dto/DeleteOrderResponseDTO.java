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
    @Schema(description = "order id of company")
    private Integer companyOrderId;
    @Schema(description = "true if order is deleted, false otherwise (no error)")
    private Boolean deleted;
    @Schema(description = "company name if present in request, null otherwise (case of restaurant delete)")
    private String companyName;
}
