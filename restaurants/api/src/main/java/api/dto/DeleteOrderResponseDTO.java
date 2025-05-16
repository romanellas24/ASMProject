package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteOrderResponseDTO {
    private Integer orderId;
    private Boolean deleted;
}
