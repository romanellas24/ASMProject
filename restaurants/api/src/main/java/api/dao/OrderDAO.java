package api.dao;

import api.entity.Order;
import api.utils.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderDAO extends JpaRepository<Order, Integer> {
    @EntityGraph(attributePaths = "dishOrders")
    Optional<Order> findById(Integer id);
    Page<Order> findAllByStatusAndDeliveryTimeAfterAndDeliveryTimeBeforeOrderByDeliveryTimeAsc(OrderStatus orderStatus, LocalDateTime now, LocalDateTime endOfDay, Pageable pageable);
}
