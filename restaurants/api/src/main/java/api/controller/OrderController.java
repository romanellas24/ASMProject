package api.controller;

import api.dto.*;
import api.exception.InvalidDateTimeFormat;
import api.exception.NotFoundException;
import api.service.DishService;
import api.service.MenuService;
import api.service.OrderService;
import api.service.RabbitService;
import api.utils.PendingRequests;
import api.utils.StringToDate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/order")
@Tag(name="Order")
public class OrderController {

    @Autowired
    private DishService dishService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RabbitService rabbitService;

    @Autowired
    private PendingRequests pendingRequests;
    @Autowired
    private MenuService menuService;

    @GetMapping("/{id}")
    @ResponseBody
    @Operation(description = "Get dish by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "dish found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "id not found")
    })
    public OrderDTO getOrder(
            @Parameter(description = "order's id", required = true, example = "1")
            @PathVariable("id") Integer id) throws Exception {
        return orderService.getOrder(id);
    }

    @GetMapping
    @ResponseBody
    @Operation(description = "get all orders id day")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "dish found",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "400", description = "invalid day format")
    })
    public List<OrderDTO> getOrdersByDay(
            @Parameter(name = "date", description = "day used to retrieve orders. Default is current date")
            @RequestParam(value = "date", required = false) String day,
            @Parameter(name = "page", description = "the request is pages. Use this value to retrieve different items. Default is 0")
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page){
        LocalDate date;
        if (day == null){
            date = LocalDate.now();
        } else {
            if (!StringToDate.isStringLocalDateValid(day)) {
                throw new InvalidDateTimeFormat("invalid day format");
            }
            date = StringToDate.convertStringToLocalDate(day);
        }
        return orderService.getOrdersByDayPaged(date, page);
    }

    @PostMapping
    @ResponseBody
    @Operation(
            summary = "Create a new order",
            description = "Creates a new order from a list of dish info (id - quantity). The deliveryTime must be a valid datetime string. If dish IDs are not in the menu for the specified date, an error is returned."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully created and being processed",
                    content = @Content(schema = @Schema(implementation = ResponseOrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date format or missing data"),
            @ApiResponse(responseCode = "404", description = "Some dish IDs not found or not in menu"),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Order creation payload including delivery time and dish infos (id - quantity)",
            content = @Content(schema = @Schema(implementation = CreateOrderDTO.class))
    )
    public CompletableFuture<ResponseOrderDTO> createOrder(@RequestBody CreateOrderDTO orderRequest) throws Exception {

        if(!StringToDate.isStringValid(orderRequest.getDeliveryTime())) {
            throw new InvalidDateTimeFormat("invalid String DateTime format.");
        }
        //this will throw error for invalid id
        dishService.checkIds(orderRequest.getDishIds());

        // check if ids are in menù
        LocalDate date = StringToDate.convertStringToLocalDateTime(orderRequest.getDeliveryTime()).toLocalDate();
        menuService.checkIdsInMenu(orderRequest.getDishIds(), date);

        String correlationId = UUID.randomUUID().toString();
        WaitingOrderDTO waitingOrderDTO = WaitingOrderDTO.from(orderRequest, correlationId);

        rabbitService.publishWaitingOrder(waitingOrderDTO);

        CompletableFuture<ResponseOrderDTO> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future, waitingOrderDTO);

        CompletableFuture.delayedExecutor(3, TimeUnit.MINUTES).execute(() -> {
            pendingRequests.timeout(correlationId);
        });

        return future;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    @Operation(
            summary = "Delete an order",
            description = "Deletes the order by ID. The object returned contains infos on the success of deletion"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully deleted",
                    content = @Content(schema = @Schema(implementation = DeleteOrderResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found"),
    })
    public DeleteOrderResponseDTO deleteOrder(
            @Parameter(description = "ID of the order to delete", required = true, example = "42")
            @PathVariable("id") Integer id) throws Exception {
        if (!orderService.existsOrder(id)) {
            throw new NotFoundException("Order not found.");
        }

        Boolean deleted = orderService.deleteOrder(id);
        DeleteOrderResponseDTO deleteOrderResponseDTO = new DeleteOrderResponseDTO(id,deleted);
        if(deleted){
            rabbitService.publishDeletedOrder(deleteOrderResponseDTO.getOrderId());
        }
        return deleteOrderResponseDTO;
    }
}
