package restaurant.service;

import restaurant.dto.DecisionOrderDTO;
import restaurant.dto.OrderDTO;
import restaurant.dto.WebSocketEventDTO;

public interface RabbitService {
//    void publishDecision(DecisionOrderDTO decisionOrderDTO);
//    void handleWaitingOrder(OrderDTO waitingOrderDTO);
//    void handleNewOrder(Integer id) throws Exception;
//    void handleDeletedOrder(Integer id) throws Exception;
//    void handleTimeoutOrder(Integer id) throws Exception;

    void handleEvent(WebSocketEventDTO eventDTO) throws Exception;
}
