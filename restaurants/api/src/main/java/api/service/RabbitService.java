package api.service;

import api.dto.DecisionOrderDTO;
import api.dto.OrderDTO;
import api.dto.WebSocketEventDTO;

public interface RabbitService {
    void handleDecision(DecisionOrderDTO decisionOrderDTO) throws Exception;
    void publishTimeout(Integer orderId);
    void publishWaitingOrder();
    void publishDeletedOrder(Integer id);
    void publishNewOrder(Integer id);

    void publishEvent(WebSocketEventDTO webSocketEventDTO);
}
