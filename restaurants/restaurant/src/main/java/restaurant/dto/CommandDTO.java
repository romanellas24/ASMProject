package restaurant.dto;

import lombok.Data;
import restaurant.utils.CommandType;

import java.io.Serializable;
import java.util.Map;

@Data
public class CommandDTO implements Serializable {
    private CommandType command;
    private Map<String, Object> payload;
}