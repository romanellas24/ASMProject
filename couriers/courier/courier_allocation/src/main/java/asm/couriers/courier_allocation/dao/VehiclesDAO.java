package asm.couriers.courier_allocation.dao;

import asm.couriers.courier_allocation.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiclesDAO extends JpaRepository<Vehicle, Integer> {
    Integer countVehiclesByStateIsFalse();
}
