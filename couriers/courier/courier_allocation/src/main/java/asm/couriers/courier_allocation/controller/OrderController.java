package asm.couriers.courier_allocation.controller;

import asm.couriers.courier_allocation.dto.*;
import asm.couriers.courier_allocation.service.AllocationService;
import asm.couriers.courier_allocation.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/order")
@Tag(name = "order", description = "Endpoints to get order info and delete. The creation of order is made when a vehicle is allocated.")
public class OrderController {

    private final AllocationService allocationService;
    private final AuthService authService;
    private final ZeebeClient zeebeClient;
    private final ObjectMapper objectMapper;
    private final String courierName;

    public OrderController(AllocationService allocationService, AuthService authService, ZeebeClient zeebeClient, ObjectMapper objectMapper, @Value("${local.server.name}") String courierName) {
        this.allocationService = allocationService;
        this.authService = authService;
        this.zeebeClient = zeebeClient;
        this.objectMapper = objectMapper;
        this.courierName = courierName;
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Operation(description = "Get order by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "order found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema
                                    (implementation = OrderInfoDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "id not found")
    })
    public OrderInfoDTO getOrder(
            @Parameter(description = "order's id", required = true, example = "1")
            @PathVariable("id") Integer id,
            @Parameter(description = "Optional company name. If provided, the order is is mapped to company order id",
                    required = false, example = "acmeat")
            @RequestParam(value = "company", required = false) String companyName) throws Exception {
        if (companyName != null && !companyName.isEmpty()) {
            CompanyDTO companyDTO = allocationService.getCompanyFromName(companyName);
            return allocationService.getOrder(id, companyDTO);
        } else {
            return allocationService.getOrder(id);
        }
    }

    @DeleteMapping("/{id}")
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
    public DeleteInfoDTO deleteOrder(@RequestBody RequestDeleteDTO request,
                                     @Parameter(description = "order's id", required = true, example = "1")
                                     @PathVariable Integer id,
                                     @Parameter(description = "company name", required = true, example = "acmeat")
                                         @RequestParam(value = "company", required = true) String companyName) throws Exception {

        if (request == null || request.getHash() == null ) {
            throw new Exception("Invalid request body");
        }

        CompanyDTO company = authService.getCompanyFromNameAndHash(companyName.toLowerCase(), request.getHash());

        Integer idToDelete = allocationService.getOrder(id, company).getOrderId();


        Map<String, Object> variables = new java.util.HashMap<>(Map.of());
        variables.put("idToDelete", idToDelete);
        variables.put("courier", this.courierName);
        variables.put("type_request", "delete");

        ProcessInstanceResult result = zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("Process_0968zwe")
                .latestVersion()
                .variables(variables)
                .withResult()
                .send()
                .join();

        return objectMapper.convertValue(result.getVariablesAsMap().get("result"), DeleteInfoDTO.class);
    }

}
