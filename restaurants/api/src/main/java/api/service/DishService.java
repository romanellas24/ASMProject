package api.service;

import api.dto.DishDTO;
import api.exception.InvalidDishId;

import java.util.List;

public interface DishService {
    void checkIds(Integer[] ids) throws InvalidDishId;
    void checkId(Integer id) throws InvalidDishId;
    DishDTO getDish(Integer id);
    List<DishDTO> getAll();
}
