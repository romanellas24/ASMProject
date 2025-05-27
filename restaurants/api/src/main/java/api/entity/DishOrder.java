package api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dish_order")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DishOrder {

    @EmbeddedId
    private DishOrderId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("dishId")
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @Column(name = "mult")
    private Integer multiplicative;
}
