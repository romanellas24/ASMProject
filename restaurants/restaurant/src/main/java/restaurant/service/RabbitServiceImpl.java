package restaurant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import restaurant.WSHandler;
import restaurant.dto.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.utils.RabbitConfig;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RabbitServiceImpl implements RabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WSHandler wsHandler;

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private ObjectMapper objectMapper;

//    @Override
//    public void publishDecision(DecisionOrderDTO decisionOrderDTO) {
//        rabbitTemplate.convertAndSend(
//                RabbitConfig.ORDER_EXCHANGE,
//                RabbitConfig.DECISIONS_ROUTING_KEY,
//                decisionOrderDTO
//        );
//    }
//
//    @Override
//    @RabbitListener(queues = RabbitConfig.WAITING_ORDERS_QUEUE)
//    public void handleWaitingOrder(OrderDTO waitingOrderDTO) {
//        try {
//            wsHandler.putOrder(waitingOrderDTO);
//        } catch (Exception e) {
////            this.publishDecision(new DecisionOrderDTO((String) waitingOrderDTO.getId(), false));
//        }
//    }
//
//    @Override
//    @RabbitListener(queues = RabbitConfig.NEW_ORDERS_QUEUE)
//    public void handleNewOrder(Integer id) throws Exception {
//        OrderBasicInfoDTO orderBasicInfoDTO = new OrderBasicInfoDTO();
//        orderBasicInfoDTO.setId(id);
//        orderBasicInfoDTO.setDeleted(false);
//        wsHandler.sendChangesInOrder(orderBasicInfoDTO);
//    }
//
//    @Override
//    @RabbitListener(queues = RabbitConfig.DELETED_ORDERS_QUEUE)
//    public void handleDeletedOrder(Integer id) throws Exception {
//        OrderBasicInfoDTO orderBasicInfoDTO = new OrderBasicInfoDTO();
//        orderBasicInfoDTO.setId(id);
//        orderBasicInfoDTO.setDeleted(true);
//        wsHandler.sendChangesInOrder(orderBasicInfoDTO);
//    }

    //@Override
    @RabbitListener(queues = RabbitConfig.TIMEOUT_ORDERS_QUEUE)
    public void handleTimeoutOrder(Integer id) throws Exception {
        wsHandler.deleteTimeoutOrder(id);
    }

    @Override
    @RabbitListener(queues = RabbitConfig.EVENT_QUEUE)
    public void handleEvent(WebSocketEventDTO eventDTO) throws Exception {
        log.info("event occurred: {}", eventDTO);
        switch (eventDTO.getEventType()){
            case ORDER_CAN_START -> {
                Thread.sleep(1000);
                Integer orderId = objectMapper.convertValue(eventDTO.getPayload().get("orderId"), Integer.class);
                TaskDecisionOrderDTO taskOrderDeleted = taskListService.getOrderToPrepareFromId(orderId);
                eventDTO.setPayload(Map.of("tasks", List.of(taskOrderDeleted)));
            }
            case ORDER_WAITING -> {
                Thread.sleep(1000);
                Integer orderId = objectMapper.convertValue(eventDTO.getPayload().get("orderId"), Integer.class);
                List<TaskDecisionOrderDTO> tasks = taskListService.getWaitingOrderTasksWithId(orderId);
                eventDTO.setPayload(Map.of("tasks", tasks));
            }
        }
        wsHandler.sendEvent(eventDTO);
    }

}
