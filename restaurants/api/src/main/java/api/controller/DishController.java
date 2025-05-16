package api.controller;

import api.dao.DishDAO;
import api.dto.DishDTO;
import api.exception.InvalidDishId;
import api.exception.NotFoundException;
import api.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/{id}")
    @ResponseBody
    public DishDTO getDish(@PathVariable("id") Integer id) throws InvalidDishId, Exception {
        //check if id exists
        dishService.checkId(id);
        return dishService.getDish(id);
    }

    @GetMapping
    @ResponseBody
    public List<DishDTO> getAllDishes() {
        return dishService.getAll();
    }
}
