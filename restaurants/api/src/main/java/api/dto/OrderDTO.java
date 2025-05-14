package api.dto;

import api.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Integer id;
    private LocalDateTime deliveryTime;
    private DishOrderDTO[] dishes;
    private Double totalPrice;

    public static OrderDTO fromOrder(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setDeliveryTime(order.getDeliveryTime());
        dto.setDishes(DishOrderDTO.from(order.getDishOrders()));
        dto.setTotalPrice(
                Stream.of(dto.getDishes())
                        .map(DishOrderDTO::getDish)
                        .mapToDouble(DishDTO::getPrice)
                        .sum()
        );
        return dto;
    }
}
