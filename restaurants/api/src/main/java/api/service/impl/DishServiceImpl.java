package api.service.impl;

import api.dao.DishDAO;
import api.exception.InvalidDishId;
import api.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishDAO dishDAO;

    @Override
    public void checkIds(Integer[] ids) throws InvalidDishId {
        for (Integer id : ids) {
            if(!dishDAO.existsById(id))
                throw new InvalidDishId("Invalid dish id: " + id);
        }
    }
}
