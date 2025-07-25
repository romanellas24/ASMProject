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
@Schema(description = "Request to allocate a vehicle for an order")
public class RequestAllocateDTO {

    @Schema(description="order in company - 1 in acmeat")
    private Integer orderId;

    @Schema(description = "ID of the vehicle to be allocated", example = "12")
    private Integer vehicle;

    @Schema(description = "Estimated delivery duration in minutes", example = "40")
    private Integer timeMinutes;

    @Schema(description = "Expected delivery time in format yyyy-MM-dd HH:mm", format = "yyyy-MM-dd HH:mm", example = "2025-05-26 14:30")
    private String expectedDeliveryTime;

    @Schema(description = "Company name requesting the allocation", example = "acmeat")
    private String companyName;

    @Schema(description = "Authentication hash for the request", example = "a1b2c3d4e5f6")
    private String hash;

    @Schema(description = "Restaurant address, in human readable format.")
    private String localAddress;

    @Schema(description = "Client address")
    private String userAddress;
}