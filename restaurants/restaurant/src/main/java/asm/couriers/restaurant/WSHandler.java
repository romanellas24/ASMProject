package asm.couriers.restaurant;

import asm.couriers.restaurant.dto.DecisionOrderDTO;
import asm.couriers.restaurant.dto.OrderBasicInfoDTO;
import asm.couriers.restaurant.dto.WaitingOrderDTO;
import asm.couriers.restaurant.rabbitmq.RabbitService;
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
    private Map<String, WaitingOrderDTO> waitingOrders = new ConcurrentHashMap<>();

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
        for (WaitingOrderDTO waitingOrderDTO : waitingOrders.values()) {
            this.sendOrder(session, waitingOrderDTO);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // decode to DTO
        String payload = message.getPayload().toString();
        try {
            DecisionOrderDTO decision = objectMapper.readValue(payload, DecisionOrderDTO.class);
            waitingOrders.remove(decision.getCorrelationId());
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

    public void putOrder(WaitingOrderDTO waitingOrderDTO) throws Exception {
        waitingOrders.put(waitingOrderDTO.getCorrelationID(), waitingOrderDTO);
        for(WebSocketSession session : sessions.values()) {
            this.sendOrder(session, waitingOrderDTO);
        }
    }

    public void sendOrder(WebSocketSession currentSession, WaitingOrderDTO waitingOrderDTO) throws Exception {
        String objString = objectMapper.writeValueAsString(waitingOrderDTO);
        currentSession.sendMessage(new TextMessage(objString));
        log.info("Sent order {} to restaurateur", waitingOrderDTO.getCorrelationID());
    }

    public void sendChangesInOrder(OrderBasicInfoDTO orderBasicInfoDTO) throws Exception {
        String objString = objectMapper.writeValueAsString(orderBasicInfoDTO);
        for(WebSocketSession session : sessions.values()) {
            session.sendMessage(new TextMessage(objString));
        }
        log.info("Sent order {}: {} to restaurateur", orderBasicInfoDTO.getDeleted() ? "deleted" : "added", orderBasicInfoDTO.getId());
    }


}