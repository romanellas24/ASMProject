package api.controller;

import api.dto.CreateOrderDTO;
import api.dto.OrderDTO;
import api.dto.ResponseOrderDTO;
import api.dto.WaitingOrderDTO;
import api.exception.InvalidDateTimeFormat;
import api.exception.NotFoundException;
import api.service.DishService;
import api.service.OrderService;
import api.service.RabbitService;
import api.utils.PendingRequests;
import api.utils.StringToLocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public OrderDTO getOrder(@RequestParam Integer id) throws NotFoundException, Exception {
        return orderService.getOrder(id);
    }

    @PostMapping
    @ResponseBody
    public CompletableFuture<ResponseOrderDTO> createOrder(@RequestBody CreateOrderDTO orderRequest) throws Exception {

        if(!StringToLocalDateTime.isStringValid(orderRequest.getDeliveryTime())) {
            throw new InvalidDateTimeFormat("invalid String DateTime format.");
        }
        //this will throw error for invalid id
        dishService.checkIds(orderRequest.getDishIds());

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
}
