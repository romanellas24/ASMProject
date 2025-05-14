package api.service.impl;

import api.dto.DecisionOrderDTO;
import api.dto.OrderDTO;
import api.dto.ResponseOrderDTO;
import api.dto.WaitingOrderDTO;
import api.exception.ServerException;
import api.service.OrderService;
import api.service.RabbitService;
import api.utils.PendingRequests;
import api.utils.RabbitConfig;
import jakarta.transaction.Transactional;
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

        String correlationId = decisionOrderDTO.getCorrelationId();

        if(!pendingRequests.checkExists(correlationId)) {
            return;
        }

        if(!decisionOrderDTO.getAccepted()){
            pendingRequests.complete(correlationId, new ResponseOrderDTO(false,null));
            return;
        }

        WaitingOrderDTO waitingOrder = pendingRequests.getWaitingOrder(correlationId);

        if(waitingOrder == null) {
            throw new ServerException("Internal server error: Waiting order not found");
        }

        Integer orderId = orderService.createOrder(waitingOrder.getDishIds(), waitingOrder.getOrderTime());
        OrderDTO order = orderService.getOrder(orderId);
        pendingRequests.complete(correlationId, new ResponseOrderDTO(true,order));
    }

    @Override
    public void publishWaitingOrder(WaitingOrderDTO waitingOrderDTO) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EXCHANGE,
                RabbitConfig.WAITING_ORDERS_ROUTING_KEY,
                waitingOrderDTO
        );
    }
}
