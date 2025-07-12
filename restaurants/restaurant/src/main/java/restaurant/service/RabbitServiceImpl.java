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
    private WSHandler wsHandler;

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private ObjectMapper objectMapper;


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
