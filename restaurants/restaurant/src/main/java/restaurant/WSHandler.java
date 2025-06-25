package restaurant;

import restaurant.dto.DecisionOrderDTO;
import restaurant.dto.OrderBasicInfoDTO;
import restaurant.dto.OrderDTO;
import restaurant.rabbitmq.RabbitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WSHandler implements WebSocketHandler {

    @Autowired
    RabbitService rabbit;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<Integer, OrderDTO> ordersInPending = new ConcurrentHashMap<>();

    // to send object (dto)
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        session.sendMessage(new TextMessage("Connected"));
        log.info("connection established");
        for (OrderDTO waitingOrderDTO : ordersInPending.values()) {
            this.sendOrder(session, waitingOrderDTO);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // decode to DTO
        String payload = message.getPayload().toString();
        try {
            DecisionOrderDTO decision = objectMapper.readValue(payload, DecisionOrderDTO.class);
            ordersInPending.remove(decision.getId());
            rabbit.publishDecision(decision);
            for(WebSocketSession sessionActive : sessions.values()){
                String objString = objectMapper.writeValueAsString(decision);
                if (!sessionActive.getId().equals(session.getId())){
                    sessionActive.sendMessage(new TextMessage(objString));
                }
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

    public void putOrder(OrderDTO orderPendingDTO) throws Exception {
        ordersInPending.put(orderPendingDTO.getId(), orderPendingDTO);
        log.info("Waiting order {} added", orderPendingDTO.getId());
        for(WebSocketSession session : sessions.values()) {
            this.sendOrder(session, orderPendingDTO);
        }
    }

    public void sendOrder(WebSocketSession currentSession, OrderDTO orderDTO) throws Exception {
        String objString = objectMapper.writeValueAsString(orderDTO);
        currentSession.sendMessage(new TextMessage(objString));
        log.info("Sent order {} to restaurateur.", orderDTO.getId());
    }

    public void sendChangesInOrder(OrderBasicInfoDTO orderBasicInfoDTO) throws Exception {
        String objString = objectMapper.writeValueAsString(orderBasicInfoDTO);
        for(WebSocketSession session : sessions.values()) {
            session.sendMessage(new TextMessage(objString));
        }
        log.info("Sent order {}: {} to restaurateur", orderBasicInfoDTO.getDeleted() ? "deleted" : "added", orderBasicInfoDTO.getId());
    }


}