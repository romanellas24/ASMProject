package api.dto;

import api.utils.StringToLocalDateTime;
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
    private Integer[] dishIds;

    public static WaitingOrderDTO from(CreateOrderDTO createOrderDTO, String correlationID) {
        WaitingOrderDTO waitingOrderDTO = new WaitingOrderDTO();
        waitingOrderDTO.setCorrelationID(correlationID);
        waitingOrderDTO.setOrderTime(StringToLocalDateTime.convertStringToLocalDateTime(createOrderDTO.getDeliveryTime()));
        waitingOrderDTO.setDishIds(createOrderDTO.getDishIds());
        return waitingOrderDTO;
    }
}
