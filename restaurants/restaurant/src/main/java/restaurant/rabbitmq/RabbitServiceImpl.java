package restaurant.rabbitmq;

import restaurant.WSHandler;
import restaurant.dto.DecisionOrderDTO;
import restaurant.dto.OrderBasicInfoDTO;
import restaurant.dto.WaitingOrderDTO;
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
            wsHandler.putOrder(waitingOrderDTO);
        } catch (Exception e) {
            this.publishDecision(new DecisionOrderDTO(waitingOrderDTO.getCorrelationID(), false));
        }
    }

    @Override
    @RabbitListener(queues = RabbitConfig.NEW_ORDERS_QUEUE)
    public void handleNewOrder(Integer id) throws Exception {
        OrderBasicInfoDTO orderBasicInfoDTO = new OrderBasicInfoDTO();
        orderBasicInfoDTO.setId(id);
        orderBasicInfoDTO.setDeleted(false);
        wsHandler.sendChangesInOrder(orderBasicInfoDTO);
    }

    @Override
    @RabbitListener(queues = RabbitConfig.DELETED_ORDERS_QUEUE)
    public void handleDeletedOrder(Integer id) throws Exception {
        OrderBasicInfoDTO orderBasicInfoDTO = new OrderBasicInfoDTO();
        orderBasicInfoDTO.setId(id);
        orderBasicInfoDTO.setDeleted(true);
        wsHandler.sendChangesInOrder(orderBasicInfoDTO);
    }


}
