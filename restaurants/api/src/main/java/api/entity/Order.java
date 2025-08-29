package api.entity;

import api.utils.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@ToString(exclude = "dishes")
@Setter
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "delivery_time", nullable = false)
    private LocalDateTime deliveryTime;

    @Enumerated(EnumType.STRING) // Dice a JPA di salvare il nome dell'enum ("PENDING", "ACCEPTED", etc.) nel database
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "created_at",insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DishOrder> dishOrders = new HashSet<>();

    public Order(LocalDateTime deliveryTime){
        this.deliveryTime = deliveryTime;
    }
}
