package api.service;

import api.exception.InvalidDishId;

public interface DishService {
    void checkIds(Integer[] ids) throws InvalidDishId;
}
