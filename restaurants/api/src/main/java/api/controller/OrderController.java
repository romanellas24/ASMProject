package api.controller;

import api.dao.OrderMappingDAO;
import api.dto.*;
import api.entity.Order;
import api.exception.CompanyIdException;
import api.exception.InvalidDateTimeFormat;
import api.exception.NotFoundException;
import api.service.DishService;
import api.service.MenuService;
import api.service.OrderService;
import api.service.RabbitService;
import api.utils.OrderStatus;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

@Slf4j
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
            @PathVariable("id") Integer id,
            @Parameter(description = "Optional company name. If provided, the order is is mapped to company order id",
                    required = false, example = "acmeat")
            @RequestParam(value = "company", required = false) String companyName) throws Exception {
        if (companyName != null && !companyName.isEmpty()) {
            companyName = companyName.toLowerCase();
            return orderService.getOrder(id, companyName);
        } else {
            return orderService.getOrder(id);
        }
    }

    @GetMapping
    @ResponseBody
    @Operation(description = "get all orders id day (ONLY order ACCEPTED)")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "dish found",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "400", description = "invalid day format",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))
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
            @ApiResponse(responseCode = "400", description = "Invalid date format or missing data",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Some dish IDs not found or not in menu",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))
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

        orderRequest.setCompanyName(orderRequest.getCompanyName().toLowerCase());

        if (!orderService.isIdCompanyValid(orderRequest.getId(), orderRequest.getCompanyName())){
            throw new CompanyIdException("Company order id already exists");
        }

        //this will throw error for invalid id
        dishService.checkIds(orderRequest.getDishIds());

        // check if ids are in menù
        LocalDate date = StringToDate.convertStringToLocalDate(orderRequest.getDeliveryTime());
        menuService.checkIdsInMenu(orderRequest.getDishIds(), date);

        LocalDateTime dateTime = StringToDate.convertStringToLocalDateTime(orderRequest.getDeliveryTime());

        //add order in db with status PENDING
        Integer orderId  = orderService.createOrder(orderRequest.getDishes(), StringToDate.convertStringToLocalDateTime(orderRequest.getDeliveryTime()));

        DishDTO[] dishes = dishService.getDishes(orderRequest.getDishIds());

        OrderDTO orderPendingDTO = OrderDTO.fromOrderRequest(orderRequest, dateTime, dishes, orderId);

        rabbitService.publishWaitingOrder(orderPendingDTO);

        log.info("waiting order: {} send in rabbit queue.", orderPendingDTO);

        CompletableFuture<ResponseOrderDTO> future = new CompletableFuture<>();
        pendingRequests.put(future, orderId);

        CompletableFuture.delayedExecutor(3, TimeUnit.MINUTES).execute(() -> {
            try {
                orderService.updateOrderStatus(orderId, OrderStatus.TIMED_OUT);
                log.warn("Order {} has been timed out.", orderId);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
            rabbitService.publishTimeout(orderId);
            pendingRequests.timeout(orderId);
        });

        return future.thenApply(responseOrderDTO -> {
            try{
                if (responseOrderDTO.isAccepted()){
                    rabbitService.publishNewOrder(responseOrderDTO.getOrder().getId());
                    OrderMappingDTO orderMappingDTO = new OrderMappingDTO();
                    orderMappingDTO.setCompanyName(orderRequest.getCompanyName());
                    orderMappingDTO.setCompanyId(orderRequest.getId());
                    orderMappingDTO.setOrderId(responseOrderDTO.getOrder().getId());

                    orderService.updateOrderStatus(responseOrderDTO.getOrder().getId(), OrderStatus.ACCEPTED);
                    orderService.saveMapping(responseOrderDTO, orderMappingDTO);
                } else {
                    orderService.updateOrderStatus(responseOrderDTO.getOrder().getId(), OrderStatus.REJECTED);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return responseOrderDTO;

        }).exceptionally(ex -> {
            log.warn(ex.getMessage());
            throw new CompletionException(ex);
        });
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
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ExceptionDTO.class))),
    })
    public DeleteOrderResponseDTO deleteOrder(
            @Parameter(description = "ID of the order to delete.", required = true, example = "42")
            @PathVariable("id") Integer id,
            @Parameter(description = "Optional company name. If provided, the order will be mapped to this company before deletion.",
                    required = false, example = "acmeat")
            @RequestParam(value = "company", required = false) String companyName) throws Exception {


        DeleteOrderResponseDTO deleteOrderResponseDTO = (companyName == null || companyName.isEmpty()) ? deleteOrderRest(id) : deleteOrderCompany(id,companyName);
        if(deleteOrderResponseDTO.getDeleted()){
            rabbitService.publishDeletedOrder(deleteOrderResponseDTO.getOrderId());
            log.info("deleted order: {}.",deleteOrderResponseDTO.getOrderId());
        }
        return deleteOrderResponseDTO;
    }

    private DeleteOrderResponseDTO deleteOrderCompany(Integer id, String companyName) throws Exception {

        companyName = companyName.toLowerCase();
        log.info("deletion of  order {} of company {}", id, companyName);
        OrderMappingDTO mapping = orderService.getMapping(companyName, id);

        Boolean deleted = orderService.deleteOrder(mapping);

        return new DeleteOrderResponseDTO(mapping.getOrderId(), mapping.getCompanyId(), deleted,mapping.getCompanyName());
    }

    private DeleteOrderResponseDTO deleteOrderRest(Integer id) throws Exception {
        if (!orderService.existsOrder(id)) {
            throw new NotFoundException("Order not found.");
        }

        Boolean deleted = orderService.deleteOrder(id);

        return new DeleteOrderResponseDTO(id, null, deleted, null);
    }
}
