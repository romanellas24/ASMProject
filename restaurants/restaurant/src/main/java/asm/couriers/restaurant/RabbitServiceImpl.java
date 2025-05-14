package asm.couriers.restaurant;

import asm.couriers.restaurant.dto.DecisionOrderDTO;
import asm.couriers.restaurant.dto.WaitingOrderDTO;
import asm.couriers.restaurant.rabbitmq.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitServiceImpl implements RabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WSHandler wsHandler;

    @Override
    public void publishDecision(DecisionOrderDTO decisionOrderDTO) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EXCHANGE,
                RabbitConfig.DECISIONS_ROUTING_KEY,
                decisionOrderDTO
        );
    }

    @Override
    @RabbitListener(queues = RabbitConfig.WAITING_ORDERS_QUEUE)
    public void handleWaitingOrder(WaitingOrderDTO waitingOrderDTO) {
        try {
            wsHandler.sendOrder(waitingOrderDTO);
        } catch (Exception e) {
            this.publishDecision(new DecisionOrderDTO(waitingOrderDTO.getCorrelationID(), false));
        }
    }
}
