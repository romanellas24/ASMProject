package asm.couriers.courier_allocation.controller;

import asm.couriers.courier_allocation.dto.*;
import asm.couriers.courier_allocation.exception.BadData;
import asm.couriers.courier_allocation.exception.InvalidDateTimeFormat;
import asm.couriers.courier_allocation.service.AllocationService;
import asm.couriers.courier_allocation.service.AuthService;
import asm.couriers.courier_allocation.service.MapsService;
import asm.couriers.courier_allocation.utils.StringToLocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/allocation")
@Tag(name = "allocation", description = "Endpoints to handle allocation of delivery vehicles")
public class AllocationController {

    private final AllocationService allocationService;
    private final AuthService authService;
    private final ZeebeClient zeebeClient;
    private final String courierName;
    private final ObjectMapper objectMapper;

    public AllocationController(AllocationService allocationService,
                                AuthService authService,
                                ZeebeClient zeebeClient,
                                @Value("${local.server.name}") String courierName,
                                ObjectMapper objectMapper){
        this.allocationService = allocationService;
        this.authService = authService;
        this.zeebeClient = zeebeClient;
        this.courierName = courierName;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/availability-check")
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

        if (request.getLocalAddress() == null || request.getUserAddress() == null){
            throw new BadData("invalid address");
        }



        LocalDateTime dateTimeDelivery = StringToLocalDateTime.convertStringToLocalDateTime(request.getDeliveryTime());

        Map<String, Object> variables = new HashMap<>();
        variables.put("request", request);
        variables.put("dateTime", dateTimeDelivery);
        variables.put("courier", this.courierName);
        variables.put("type_request", "check");

        ProcessInstanceResult result = zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("Process_0968zwe")
                .latestVersion()
                .variables(variables)
                .withResult()
                .send()
                .join();

        return objectMapper.convertValue(result.getVariablesAsMap().get("availability"), AvailabilityDTO.class);
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
            throw new BadData("Invalid request body");
        }

        if (request.getOrderId()== null) {
            throw new BadData("invalid order id");
        }

        if (!StringToLocalDateTime.isStringValid(request.getExpectedDeliveryTime())) {
            throw new InvalidDateTimeFormat("Invalid date format");
        }

        CompanyDTO company = authService.getCompanyFromNameAndHash(request.getCompanyName(), request.getHash());

        if (!allocationService.isOrderCompanyValid(request.getOrderId(), company)) {
            throw new BadData("order id already exists for this company");
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("request", request);
        variables.put("company", company);
        variables.put("courier", this.courierName);
        variables.put("type_request", "save");

        ProcessInstanceResult result = zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("Process_0968zwe")
                .latestVersion()
                .variables(variables)
                .withResult()
                .send()
                .join();

        Map<String, Object> resultVariables = result.getVariablesAsMap();

        if (Boolean.FALSE.equals(resultVariables.get("available"))) {
            throw new BadData("vehicle id not available");
        }

        return objectMapper.convertValue(resultVariables.get("order"), OrderInfoDTO.class);
    }




}
