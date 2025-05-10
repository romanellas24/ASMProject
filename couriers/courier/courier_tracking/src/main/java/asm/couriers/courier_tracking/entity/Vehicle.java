package asm.couriers.courier_tracking.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/*
    State 0: Veicolo disponibile, non occupato, rientrato
    State 1: Veicolo non disponibile, partito
 */

@Entity
@Setter
@Getter
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name="available")
    Boolean available;
}
