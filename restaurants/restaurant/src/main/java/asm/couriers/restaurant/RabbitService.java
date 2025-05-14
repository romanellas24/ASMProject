package asm.couriers.restaurant;

import asm.couriers.restaurant.dto.DecisionOrderDTO;
import asm.couriers.restaurant.dto.WaitingOrderDTO;

public interface RabbitService {
    void publishDecision(DecisionOrderDTO decisionOrderDTO);
    void handleWaitingOrder(WaitingOrderDTO waitingOrderDTO);
}
