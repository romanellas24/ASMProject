package api.service.impl;

import api.dao.DishDAO;
import api.dto.DishDTO;
import api.entity.Dish;
import api.exception.InvalidDishId;
import api.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishDAO dishDAO;

    @Override
    public void checkIds(Integer[] ids) throws InvalidDishId {
        for (Integer id : ids) {
            this.checkId(id);
        }
    }

    @Override
    public void checkId(Integer id) throws InvalidDishId {
        if(!dishDAO.existsById(id))
            throw new InvalidDishId("Invalid dish id: " + id);
    }

    @Override
    public DishDTO getDish(Integer id) {
        Dish dish = dishDAO.getById(id);
        return DishDTO.from(dish);
    }

    @Override
    public List<DishDTO> getAll() {
        return dishDAO.findAll().stream().map(DishDTO::from).toList();
    }
}
