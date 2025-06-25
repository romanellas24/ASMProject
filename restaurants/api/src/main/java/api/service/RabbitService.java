package api.service;

import api.dto.DecisionOrderDTO;
import api.dto.OrderDTO;

public interface RabbitService {
    void handleDecision(DecisionOrderDTO decisionOrderDTO) throws Exception;
    void publishTimeout(Integer orderId);
    void publishWaitingOrder(OrderDTO waitingOrderDTO);
    void publishDeletedOrder(Integer id);
    void publishNewOrder(Integer id);
}
