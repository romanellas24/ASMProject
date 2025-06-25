package api.dto;

import api.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.IntStream;
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
                        .mapToDouble(d -> d.getDish().getPrice() * d.getQuantity())
                        .sum()
        );
        return dto;
    }

    public static OrderDTO fromOrderRequest(CreateOrderDTO createOrderDTO, LocalDateTime date, DishDTO[] dishes, Integer orderId) {

        DishBasicInfoDTO[] basicInfoDishes = createOrderDTO.getDishes();

        DishOrderDTO[] dishOrderDTOs =  IntStream.range(0, dishes.length)
                .mapToObj(
                        i -> new DishOrderDTO(
                                dishes[i],
                                basicInfoDishes[i].getQuantity()
                        )
                ).toArray(DishOrderDTO[]::new);
        double totalPrice = Arrays.stream(dishOrderDTOs)
                .mapToDouble(dish -> dish.getDish().getPrice() * dish.getQuantity())
                .sum();

        OrderDTO dto = new OrderDTO();
        dto.setId(orderId);
        dto.setDeliveryTime(date);
        dto.setTotalPrice(totalPrice);
        dto.setDishes(dishOrderDTOs);

        return dto;
    }
}
