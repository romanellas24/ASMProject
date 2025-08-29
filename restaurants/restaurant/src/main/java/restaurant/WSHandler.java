package restaurant;

import restaurant.dto.*;
import restaurant.service.RabbitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import lombok.extern.slf4j.Slf4j;
import restaurant.service.TaskListService;
import restaurant.utils.EventType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WSHandler implements WebSocketHandler {

    @Autowired
    private RabbitService rabbit;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<Integer, OrderDTO> ordersInPending = new ConcurrentHashMap<>();

    // to send object (dto)
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    @Autowired
    private TaskListService taskListService;

    private void sendError(WebSocketSession session, String message) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("type", "ERROR");
            payload.put("message", message);
            String json = objectMapper.writeValueAsString(payload);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.error("Error sending WebSocket error message: {}", e.getMessage());
        }
    }

    public void sendEvent(WebSocketEventDTO event) throws Exception {
        String objString = objectMapper.writeValueAsString(event);
        for(WebSocketSession session : sessions.values()) {
            session.sendMessage(new TextMessage(objString));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        log.info("connection established");
//        List<TaskDecisionOrderDTO> tasks = taskListService.getCookTasks();
//        if (!tasks.isEmpty()) {
//            WebSocketEventDTO event = new WebSocketEventDTO(EventType.ORDER_WAITING, Map.of("tasks", tasks));
//            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(event)));
//        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = message.getPayload().toString();

        if ("ping".equals(payload)) {
            session.sendMessage(new TextMessage("pong"));
            return;
        }

        try {
            CommandDTO command = objectMapper.readValue(payload, CommandDTO.class);
            switch(command.getCommand()) {
                case COMPLETE_TASK_WAITING ->
                        taskListService.completeTask((String) command.getPayload().get("taskId"), (Boolean) command.getPayload().get("accepted"));
                case GET_ORDERS -> {
                    List<TaskDecisionOrderDTO> tasks = taskListService.getOrderToPrepareTasks();
                    if (!tasks.isEmpty()) {
                        WebSocketEventDTO event = new WebSocketEventDTO(EventType.ORDER_CAN_START, Map.of("tasks", tasks));
                        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(event)));
                    }
                }
                case GET_WAITING_ORDERS -> {
                    List<TaskDecisionOrderDTO> tasks = taskListService.getWaitingOrderTasks();
                    if (!tasks.isEmpty()) {
                        WebSocketEventDTO event = new WebSocketEventDTO(EventType.ORDER_WAITING, Map.of("tasks", tasks));
                        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(event)));
                    }
                }
                case COMPLETE_TASK_PREPARED ->
                        taskListService.completeTask((String) command.getPayload().get("taskId"), true);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Exception occured: {} on session: {}", exception.getMessage(), session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session.getId());
        log.info("Restaurateur connection closed");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void deleteTimeoutOrder(Integer id) throws Exception {
        ordersInPending.remove(id);
        String objString = objectMapper.writeValueAsString(Map.of("id", id, "timeout", true));
        for(WebSocketSession session : sessions.values()) {
            session.sendMessage(new TextMessage(objString));
        }
        log.info("Timeout for order {}.",id);
    }

}