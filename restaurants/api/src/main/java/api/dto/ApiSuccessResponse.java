package api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO standard per risposte di successo dell'API.
 */
@Data // Genera automaticamente getter, setter, toString(), equals(), hashCode()
@NoArgsConstructor // Genera un costruttore senza argomenti
public class ApiSuccessResponse {

    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiSuccessResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}