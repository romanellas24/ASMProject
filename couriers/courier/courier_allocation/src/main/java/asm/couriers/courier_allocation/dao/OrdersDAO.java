package asm.couriers.courier_allocation.dao;


import asm.couriers.courier_allocation.entity.Company;
import asm.couriers.courier_allocation.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersDAO extends JpaRepository<Order, Integer> {
    Order findByOrderId(Integer orderId);
}
