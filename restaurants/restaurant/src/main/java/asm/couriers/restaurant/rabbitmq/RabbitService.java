package asm.couriers.restaurant.rabbitmq;

import asm.couriers.restaurant.dto.DecisionOrderDTO;
import asm.couriers.restaurant.dto.WaitingOrderDTO;

public interface RabbitService {
    void publishDecision(DecisionOrderDTO decisionOrderDTO);
    void handleWaitingOrder(WaitingOrderDTO waitingOrderDTO);
    void handleNewOrder(Integer id) throws Exception;
    void handleDeletedOrder(Integer id) throws Exception;
}
