package api.dao;

import api.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDAO extends JpaRepository<Order, Integer> {
    @EntityGraph(attributePaths = "dishOrders")
    Optional<Order> findById(Integer id);
}
