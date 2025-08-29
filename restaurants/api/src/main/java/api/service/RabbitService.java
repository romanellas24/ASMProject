package api.service;

import api.dto.WebSocketEventDTO;

public interface RabbitService {
    void publishEvent(WebSocketEventDTO webSocketEventDTO);
}
