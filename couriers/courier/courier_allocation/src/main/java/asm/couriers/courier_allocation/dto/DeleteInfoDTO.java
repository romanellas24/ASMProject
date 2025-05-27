package asm.couriers.courier_allocation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Schema(description = "Response after attempting to delete an order")
public class DeleteInfoDTO {

    @Schema(description = "ID of the deleted order", example = "1023")
    private Integer orderId;

    @Schema(description = "True if the order was successfully deleted", example = "true")
    private Boolean deleted;
}