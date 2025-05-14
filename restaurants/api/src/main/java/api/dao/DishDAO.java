package api.dao;

import api.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishDAO extends JpaRepository<Dish, Integer> {
}
