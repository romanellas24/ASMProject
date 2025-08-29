package asm.couriers.courier_tracking;

import asm.couriers.courier_tracking.dao.OrdersDAO;
import asm.couriers.courier_tracking.dao.VehiclesDAO;
import asm.couriers.courier_tracking.dto.OrderDTO;

import asm.couriers.courier_tracking.dto.OrderDeliveredDTO;
import asm.couriers.courier_tracking.dto.TaskOrderDTO;
import asm.couriers.courier_tracking.entity.Vehicle;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WSCourierHandler implements WebSocketHandler {

    private final OrdersDAO ordersDAO;
    private final VehiclesDAO vehiclesDAO;
    private final TaskListService taskListService;
    private final ObjectMapper objectMapper;

    /*
    First one used for handle courier_id and sessions.
    Second to retrieve, from session_id, the courier_id.
    The third one is to retrieve order from courier_id
    The fourth one is to reduce the api calls to camunda
    The introduction of this second hashmap is crucial to not kill the o(1) search of first hashmap.
     */
    private final Map<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, Integer> sessionIds = new ConcurrentHashMap<>();
    private final Map<Integer, OrderDTO> riderOrders = new ConcurrentHashMap<>();
    private final Map<Integer, TaskOrderDTO> taskOrders = new ConcurrentHashMap<>();

    public WSCourierHandler(ObjectMapper objectMapper, OrdersDAO ordersDAO, VehiclesDAO vehiclesDAO, TaskListService taskListService) {
        this.ordersDAO = ordersDAO;
        this.vehiclesDAO = vehiclesDAO;
        this.taskListService = taskListService;
        this.objectMapper = objectMapper;
    }


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
                session.close(new CloseStatus(401, "Courier already connected"));
                return;
            }

            // update hashmaps
            sessions.put(courierId, session);
            sessionIds.put(session.getId(), courierId);
            log.info("Courier {} connected with session {}", courierId, session.getId());
            session.sendMessage(new TextMessage("Connected as courier " + courierId));
            OrderDTO order = riderOrders.get(courierId);
            if (order != null) {
                TaskOrderDTO task = this.getTaskFromOrderIdAndRiderId(order.getOrderId(), courierId);
                if (task != null) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(Map.of("type", "task", "payload", task))));
                }
            }

        } else {
            log.warn("Missing courierId in WebSocket connection");
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = message.getPayload().toString();

        try {
            Map<String, Object> msg = objectMapper.readValue(payload, new TypeReference<>() {});
            String type = (String) msg.get("type");
            if ("ping".equals(type)) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(Map.of("type", "pong"))));
                return;
            } else if ("getTask".equals(type)) {
                Integer riderId = sessionIds.get(session.getId());
                Integer orderId = riderOrders.get(riderId).getOrderId();
                TaskOrderDTO task = this.getTaskFromOrderIdAndRiderId(orderId, riderId);
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(Map.of("type", "task", "payload", task))));
                return;
            } else if("delivery_completed".equals(type)) {
                OrderDeliveredDTO orderDelivered = objectMapper.convertValue(msg.get("payload"), OrderDeliveredDTO.class);

                if (!orderDelivered.getDelivered()) {
                    session.sendMessage(new TextMessage("Error: not delivered"));
                    return;
                }
                Optional<Vehicle> courier = vehiclesDAO.findById(orderDelivered.getCourierId());
                if (courier.isEmpty()) {
                    session.sendMessage(new TextMessage("Error: courier not found"));
                    return;
                }
                taskListService.completeTask(orderDelivered.getTaskId());
                riderOrders.remove(courier.get().getId());
                taskOrders.remove(courier.get().getId());
            }
        } catch (Exception e) {
            log.error("Error processing WebSocket message: {}", e.getMessage());
        }

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
            log.info("Connection closed on session: {} with status: {}", session.getId(), closeStatus.getCode());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public TaskOrderDTO getTaskFromOrderIdAndRiderId(Integer orderId, Integer riderId){
        try{
            if(riderOrders.containsKey(riderId)){
                if(taskOrders.containsKey(riderId)){
                    return taskOrders.get(riderId);
                }
                TaskOrderDTO taskOrder = taskListService.getTaskFromOrderIdAndRiderId(orderId, riderId);
                taskOrders.put(riderId, taskOrder);
                return taskOrder;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    public void addInPendingOrder(Integer riderId, OrderDTO order) throws Exception {
        riderOrders.put(riderId, order);
        if (sessions.containsKey(riderId)) {
            WebSocketSession session = sessions.get(riderId);
            String objString = objectMapper.writeValueAsString(Map.of("type", "new_delivery"));
            session.sendMessage(new TextMessage(objString));
            log.info("Sent order to courier {}: {}", riderId, objString);
        }
    }
}