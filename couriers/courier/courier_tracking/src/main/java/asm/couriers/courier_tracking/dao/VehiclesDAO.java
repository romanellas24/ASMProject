package asm.couriers.courier_tracking.dao;

import asm.couriers.courier_tracking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiclesDAO extends JpaRepository<Vehicle, Integer> {
}
