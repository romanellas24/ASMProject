package restaurant.rabbitmq;

import restaurant.dto.DecisionOrderDTO;
import restaurant.dto.WaitingOrderDTO;

public interface RabbitService {
    void publishDecision(DecisionOrderDTO decisionOrderDTO);
    void handleWaitingOrder(WaitingOrderDTO waitingOrderDTO);
    void handleNewOrder(Integer id) throws Exception;
    void handleDeletedOrder(Integer id) throws Exception;
}
