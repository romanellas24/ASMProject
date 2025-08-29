package asm.couriers.courier_allocation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Schema(description = "Request to delete an order")
public class RequestDeleteDTO {

//    @Schema(description = "ID of the order to delete", example = "1023")
//    private Integer orderId;
//
//    @Schema(description = "Name of the company", example = "MyCompany")
//    private String companyName;

    @Schema(description = "Authentication hash", example = "a1b2c3d4e5f6")
    private String hash;
}
