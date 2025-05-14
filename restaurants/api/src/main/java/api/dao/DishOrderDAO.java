package api.dao;

import api.entity.DishOrder;
import api.entity.DishOrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DishOrderDAO extends JpaRepository<DishOrder, DishOrderId> {
    @Modifying
    @Query(value = "INSERT INTO dish_order (order_id, dish_id) VALUES (:orderId, :dishId)", nativeQuery = true)
    void insertDishOrder(@Param("orderId") Integer orderId, @Param("dishId") Integer dishId);
}
