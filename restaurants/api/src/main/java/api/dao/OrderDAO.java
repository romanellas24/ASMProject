package api.dao;

import api.entity.Order;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderDAO extends JpaRepository<Order, Integer> {
    @EntityGraph(attributePaths = "dishOrders")
    Optional<Order> findById(Integer id);

    void deleteOrderById(Integer id);

    List<Order> getAllByDeliveryTimeBefore(LocalDateTime deliveryTimeBefore);

    Page<Order> findAllByDeliveryTimeAfterAndDeliveryTimeBeforeOrderByDeliveryTimeAsc(LocalDateTime endOfDay, LocalDateTime startOfDay, Pageable pageable);
}
