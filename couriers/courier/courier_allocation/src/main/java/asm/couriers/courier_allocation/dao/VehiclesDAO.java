package asm.couriers.courier_allocation.dao;

import asm.couriers.courier_allocation.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VehiclesDAO extends JpaRepository<Vehicle, Integer> {
    Integer countVehiclesByAvailableIsTrue();
//    @Query("SELECT v FROM Vehicle v WHERE v.id NOT IN ( " +
//            "SELECT o.vehicle.id FROM Order o " +
//            "WHERE (o.start_delivery_time < :expectedDelivery AND :expectedDelivery < o.end_delivery_time) " +
//            "OR (FUNCTION('TIMESTAMPDIFF', SECOND, :expectedDelivery, o.end_delivery_time) > :deliveryTime) )")
@Query("SELECT v FROM Vehicle v WHERE v.id NOT IN (" +
        "SELECT o.vehicle.id FROM Order o WHERE o.vehicle IS NOT NULL AND " +
        "o.start_delivery_time < :endTime AND o.end_delivery_time > :startTime" +
        ")")
    List<Vehicle> findAvailableVehicles(@Param("endTime")LocalDateTime endTime,
                                        @Param("startTime")LocalDateTime startTime);
}
