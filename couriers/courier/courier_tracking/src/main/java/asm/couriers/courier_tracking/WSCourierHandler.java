package asm.couriers.courier_tracking;

import asm.couriers.courier_tracking.dao.OrdersDAO;
import asm.couriers.courier_tracking.dao.VehiclesDAO;
import asm.couriers.courier_tracking.dto.OrderDTO;

import asm.couriers.courier_tracking.dto.OrderDeliveredDTO;
import asm.couriers.courier_tracking.entity.Vehicle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WSCourierHandler implements WebSocketHandler {

    @Autowired
    private CheckOrdersScheduler checkOrdersScheduler;
    @Autowired
    private OrdersDAO ordersDAO;
    @Autowired
    private VehiclesDAO vehiclesDAO;

    /*
    First one used for handle courier_id and sessions.
    Second to retrieve, from session_id, the courier_id.
    The introduction of this second hashmap is crucial to not kill the o(1) search of first hashmap.
     */
    private final Map<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, Integer> sessionIds = new ConcurrentHashMap<>();

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
        String query = session.getUri().getQuery();
        if (query != null && query.startsWith("courierId=")) {
            Integer courierId = Integer.parseInt(query.split("=")[1]);
            //check if user is already connected
            if (sessions.containsKey(courierId)) {
                log.warn("Courier {} is already connected", courierId);
                sendError(session, "Courier already connected");
                session.close(new CloseStatus(4001, "Courier already connected"));
                return;
            }

            // update hashmaps
            sessions.put(courierId, session);
            sessionIds.put(session.getId(), courierId);
            log.info("Courier {} connected with session {}", courierId, session.getId());
            session.sendMessage(new TextMessage("Connected as courier " + courierId));
            CheckOrderTask task = new CheckOrderTask(ordersDAO, vehiclesDAO, courierId, this);
            checkOrdersScheduler.schedule(task, new CronTrigger("0 * * * * * "), courierId);
        } else {
            log.warn("Missing courierId in WebSocket connection");
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        // decode to DTO
        String payload = message.getPayload().toString();
        OrderDeliveredDTO orderDelivered =  objectMapper.readValue(payload, OrderDeliveredDTO.class);

        if (!orderDelivered.getDelivered()){
            session.sendMessage(new TextMessage("Error: not delivered"));
            return;
        }
        Optional<Vehicle> courier = vehiclesDAO.findById(orderDelivered.getCourierId());
        if (courier.isEmpty()){
            session.sendMessage(new TextMessage("Error: courier not found"));
            return;
        }
        Vehicle vehicle = courier.get();
        vehicle.setAvailable(true);
        vehiclesDAO.save(vehicle);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Exception occured: {} on session: {}", exception.getMessage(), session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

        // clean session hashmaps and task scheduler
        Integer courier = sessionIds.get(session.getId());
        if (courier != null) {
            sessions.remove(courier);
            sessionIds.remove(session.getId());
            checkOrdersScheduler.cancelSchedule(courier);
            log.info("Connection closed on session: {} with status: {}", session.getId(), closeStatus.getCode());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void notifyCourier(Integer courierId, OrderDTO order) throws Exception {
        WebSocketSession session = sessions.get(courierId);
        if (session == null || !session.isOpen()) return;
        String objString = objectMapper.writeValueAsString(order);
        session.sendMessage(new TextMessage(objString));
        log.info("Sent order to courier {}: {}", courierId, objString);
    }
}