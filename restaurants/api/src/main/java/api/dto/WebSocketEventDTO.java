package api.dto;

import api.utils.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketEventDTO {

    private EventType eventType;
    private LocalDateTime timestamp;
    private Map<String, Object> payload;

    public WebSocketEventDTO(EventType eventType) {
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
        this.payload = Map.of();
    }

    public WebSocketEventDTO(EventType eventType, Map<String, Object> payload) {
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
        this.payload = payload;
    }
}
