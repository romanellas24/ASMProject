package asm.couriers.courier_allocation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer order_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    Vehicle vehicle;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_delivery_time")
    LocalDateTime start_delivery_time;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_delivery_time")
    LocalDateTime end_delivery_time;
}
