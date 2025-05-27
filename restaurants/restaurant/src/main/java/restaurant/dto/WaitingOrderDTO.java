package restaurant.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class WaitingOrderDTO implements Serializable {
    private String correlationID;
    private LocalDateTime orderTime;
    private DishInOrderDTO[] dishes;
}
