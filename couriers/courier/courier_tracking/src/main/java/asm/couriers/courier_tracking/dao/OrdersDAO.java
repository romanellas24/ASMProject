package asm.couriers.courier_tracking.dao;


import asm.couriers.courier_tracking.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersDAO extends JpaRepository<Order, Integer> {
    Order findByVehicle_IdAndStartDeliveryTimeEquals(Integer courierId, LocalDateTime now);
    List<Order> findAllByStartDeliveryTimeEquals(LocalDateTime now);
    List<Order> findAllByStartDeliveryTimeLessThanEqual(LocalDateTime now);
}
