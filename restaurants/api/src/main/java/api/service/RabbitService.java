package api.service;

import api.dto.DecisionOrderDTO;
import api.dto.WaitingOrderDTO;

public interface RabbitService {
    void handleDecision(DecisionOrderDTO decisionOrderDTO) throws Exception;
    void publishWaitingOrder(WaitingOrderDTO waitingOrderDTO);
    void publishDeletedOrder(Integer id);
    void publishNewOrder(Integer id);
}
