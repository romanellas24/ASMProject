package asm.couriers.restaurant;

import asm.couriers.restaurant.dto.DecisionOrderDTO;
import asm.couriers.restaurant.dto.WaitingOrderDTO;
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

@Slf4j
@Component
public class WSHandler implements WebSocketHandler {

    @Autowired
    RabbitService rabbit;
    private volatile WebSocketSession currentSession = null;

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
        if (this.currentSession != null) {
            log.warn("Restaurateur already connected");
            sendError(session, "Restaurateur already connected");
            session.close(new CloseStatus(CloseStatus.NOT_ACCEPTABLE.getCode(), "restaurateur already connected"));
        } else {
            this.currentSession = session;
            session.sendMessage(new TextMessage("Connected"));
            log.info("Restaurateur connected");
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // decode to DTO
        String payload = message.getPayload().toString();
        try {
            DecisionOrderDTO decision = objectMapper.readValue(payload, DecisionOrderDTO.class);
            rabbit.publishDecision(decision);
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
        if (currentSession != null && currentSession.getId().equals(session.getId())) {
            currentSession = null;
        }
        log.info("Restaurateur connection closed");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendOrder(WaitingOrderDTO waitingOrderDTO) throws Exception {
        if (currentSession != null && currentSession.isOpen()) {
            String objString = objectMapper.writeValueAsString(waitingOrderDTO);
            currentSession.sendMessage(new TextMessage(objString));
            log.info("Sent order {} to restaurateur", waitingOrderDTO.getCorrelationID());
        } else {
            log.info("No session to send order {} to restaurateur", waitingOrderDTO.getCorrelationID());
        }
    }
}