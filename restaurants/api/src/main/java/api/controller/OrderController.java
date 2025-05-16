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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/order")
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
    public OrderDTO getOrder(@PathVariable("id") Integer id) throws NotFoundException, Exception {
        return orderService.getOrder(id);
    }

    @GetMapping
    @ResponseBody
    public List<OrderDTO> getOrdersByDay(@RequestParam(value = "date", required = false) String day, @RequestParam(value = "page", required = false, defaultValue = "0") Integer page){
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
    public DeleteOrderResponseDTO deleteOrder(@PathVariable("id") Integer id) throws Exception {
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
