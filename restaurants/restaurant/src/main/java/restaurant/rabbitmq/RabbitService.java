package restaurant.rabbitmq;

import restaurant.dto.DecisionOrderDTO;
import restaurant.dto.OrderDTO;

public interface RabbitService {
    void publishDecision(DecisionOrderDTO decisionOrderDTO);
    void handleWaitingOrder(OrderDTO waitingOrderDTO);
    void handleNewOrder(Integer id) throws Exception;
    void handleDeletedOrder(Integer id) throws Exception;
    void handleTimeoutOrder(Integer id) throws Exception;
}
