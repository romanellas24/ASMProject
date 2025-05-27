package api.dto;

import api.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Order info")
public class OrderDTO {
    @Schema(description = "order id")
    private Integer id;
    @Schema(description = "date of delivery", format = "yyyy-MM-dd HH:mm")
    private LocalDateTime deliveryTime;
    @Schema(description = "dishes in order")
    private DishOrderDTO[] dishes;
    @Schema(description = "total price")
    private Double totalPrice;

    public static OrderDTO fromOrder(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setDeliveryTime(order.getDeliveryTime());
        dto.setDishes(DishOrderDTO.from(order.getDishOrders()));
        dto.setTotalPrice(
                Stream.of(dto.getDishes())
                        .mapToDouble(d -> d.getDish().getPrice() * d.getMultiplicative())
                        .sum()
        );
        return dto;
    }
}
