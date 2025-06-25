package asm.couriers.courier_allocation;

import asm.couriers.courier_allocation.dto.*;
import asm.couriers.courier_allocation.exception.InvalidDateTimeFormat;
import asm.couriers.courier_allocation.exception.VehicleNotAvailableException;
import asm.couriers.courier_allocation.service.AllocationService;
import asm.couriers.courier_allocation.service.AuthService;
import asm.couriers.courier_allocation.service.MapsService;
import asm.couriers.courier_allocation.utils.Const;
import asm.couriers.courier_allocation.utils.StringToLocalDateTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping()
@Tag(name = "allocation", description = "Endpoints to handle allocation of delivery vehicles")
public class AllocationController {
    @Autowired
    private AllocationService allocationService;

    @Autowired
    private MapsService mapsService;

    @Autowired
    private AuthService authService;

    @GetMapping(produces = "application/json")
    @ResponseBody
    @Operation(
            summary = "Check delivery availability",
            description = "Checks if a delivery vehicle is available for the given addresses and delivery time.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Availability response",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvailabilityDTO.class))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Bad request - invalid time string format",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))
                    ),
                    @ApiResponse(responseCode = "503",
                            description = "Service distance matrix not available",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Server error",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))
                    )
            }
    )
    public AvailabilityDTO checkAvailability(RequestAvailDTO request) throws Exception {

        if (!StringToLocalDateTime.isStringValid(request.getDeliveryTime())){
            throw new InvalidDateTimeFormat("Invalid format in request");
        }

        LocalDateTime dateTimeDelivery = StringToLocalDateTime.convertStringToLocalDateTime(request.getDeliveryTime());

        /*
            Per controllo disponibilitá dato:
                * indirizzo ristorante
                * indirizzo cliente
                * orario di consegna
            controllare se un veicolo é disponibile
        */
        AvailabilityDTO availabilityDTO = mapsService.getInfoDelivery(request.getLocalAddress(),request.getUserAddress());
        try {
            Integer vehicleId = allocationService.vehicle_available(availabilityDTO.getTime(), dateTimeDelivery);
            availabilityDTO.setIsVehicleAvailable(true);
            availabilityDTO.setVehicleId(vehicleId);
        } catch (VehicleNotAvailableException e) {
            log.info("vehicles are not available");
            availabilityDTO.setIsVehicleAvailable(false);
        }

        availabilityDTO.setPrice(availabilityDTO.getDistance() * Const.PRICE_PER_KM);

        return availabilityDTO;
    }

    @Operation(
            summary = "Allocate a vehicle for a delivery order",
            description = "Allocates a specific vehicle for a delivery based on the request data.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Allocation request details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RequestAllocateDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vehicle successfully allocated",
                            content = @Content(schema = @Schema(implementation = OrderInfoDTO.class))
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Bad request - invalid time string format or invalid body",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized - invalid company name or hash",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Not found - company name or vehicle id",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))
                    )
            }
    )
    @PutMapping(produces = "application/json")
    @ResponseBody
    OrderInfoDTO allocateVehicle(@RequestBody RequestAllocateDTO request) throws Exception {

        if (request == null || request.getVehicle() == null || request.getTimeMinutes() == null || request.getExpectedDeliveryTime() == null) {
            throw new Exception("Invalid request body");
        }

        if (!StringToLocalDateTime.isStringValid(request.getExpectedDeliveryTime())) {
            throw new InvalidDateTimeFormat("Invalid date format");
        }

        CompanyDTO company = authService.getCompanyFromNameAndHash(request.getCompanyName(), request.getHash());

        return allocationService.allocate_vehicle(request, company);
    }

    @DeleteMapping(produces = "application/json")
    @ResponseBody
    @Operation(
            summary = "Delete an order",
            description = "Deletes a specific delivery order if it exists and belongs to the authenticated company.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order deletion request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RequestDeleteDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order deletion result",
                            content = @Content(schema = @Schema(implementation = DeleteInfoDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request - invalid body",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - invalid company name or hash",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found - order id",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionDTO.class))
                    )
            }
    )
    public DeleteInfoDTO deleteOrder(@RequestBody RequestDeleteDTO request) throws Exception {

        if (request == null || request.getOrderId() == null || request.getHash() == null || request.getCompanyName()== null) {
            throw new Exception("Invalid request body");
        }

        CompanyDTO company = authService.getCompanyFromNameAndHash(request.getCompanyName(), request.getHash());

        Boolean deleted = allocationService.delete_order(request.getOrderId(), company);

        DeleteInfoDTO deleteInfoDTO = new DeleteInfoDTO();
        deleteInfoDTO.setOrderId(request.getOrderId());
        deleteInfoDTO.setDeleted(deleted);

        return deleteInfoDTO;
    }


}
