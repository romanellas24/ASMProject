package api.service.impl;

import api.dto.*;
import api.service.OrderService;
import api.service.RabbitService;
import api.utils.PendingRequests;
import api.utils.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitServiceImpl implements RabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private PendingRequests pendingRequests;
    @Autowired
    private OrderService orderService;


    @Override
    @RabbitListener(queues = RabbitConfig.DECISIONS_QUEUE)
    public void handleDecision(DecisionOrderDTO decisionOrderDTO) throws Exception {

        Integer id = decisionOrderDTO.getId();

        if(!pendingRequests.checkExists(id)) {
            return;
        }

        if(!decisionOrderDTO.getAccepted()){
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(id);
            pendingRequests.complete(id, new ResponseOrderDTO(false, orderDTO));
            return;
        }

        OrderDTO orderPendingDto =  orderService.getOrder(id);

        pendingRequests.complete(id, new ResponseOrderDTO(true, orderPendingDto));
    }

    @Override
    public void publishTimeout(Integer orderId) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EXCHANGE,
                RabbitConfig.TIMEOUT_ORDERS_ROUTING_KEY,
                orderId
        );
    }


    @Override
    public void publishWaitingOrder() {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EXCHANGE,
                RabbitConfig.WAITING_ORDERS_ROUTING_KEY
        );
    }

    @Override
    public void publishDeletedOrder(Integer id) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EXCHANGE,
                RabbitConfig.DELETED_ORDERS_ROUTING_KEY,
                id
        );
    }

    @Override
    public void publishNewOrder(Integer id) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EXCHANGE,
                RabbitConfig.NEW_ORDERS_ROUTING_KEY,
                id
        );
    }

    @Override
    public void publishEvent(WebSocketEventDTO webSocketEventDTO) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EXCHANGE,
                RabbitConfig.EVENT_ROUTING_KEY,
                webSocketEventDTO
        );
    }
}
