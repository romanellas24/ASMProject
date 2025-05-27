package api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "responde to create order request.")
public class ResponseOrderDTO {
    @Schema(description = "true if order is accepted by local, false otherwise")
    private boolean accepted;
    @Schema(description = "in case of accepted=true, this will contain order info. Null otherwise")
    private OrderDTO order;
}
