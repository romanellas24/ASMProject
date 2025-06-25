package restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Integer id;

    private LocalDateTime deliveryTime;

    private DishOrderDTO[] dishes;

    private Double totalPrice;

}
