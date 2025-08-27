package api.service.impl;

import api.dao.DailyMenuDAO;
import api.dao.DishDAO;
import api.dto.MenuDTO;
import api.entity.DailyMenu;
import api.entity.DailyMenuId;
import api.exception.InvalidDishId;
import api.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private DailyMenuDAO dailyMenuDAO;

    @Autowired
    private DishDAO dishDAO;

    @Override
    public MenuDTO getMenu(LocalDate date) {
        List<DailyMenu> dishes = dailyMenuDAO.findByIdDay(date);
        return MenuDTO.from(dishes, date);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(LocalDate date, List<Integer> dishIds) throws InvalidDishId {

        //clear if menu was already updated
        if (dailyMenuDAO.existsByIdDay(date)) {
            dailyMenuDAO.deleteAllByIdDay(date);
        }

        for (Integer dishId : dishIds) {
            if(!dishDAO.existsById(dishId)) {
                throw new InvalidDishId("Menu not changed. Invalid dish id: " + dishId);
            }

            DailyMenuId dailyMenuId = new DailyMenuId(date, dishId);
            DailyMenu toSave = new DailyMenu(dailyMenuId);
            dailyMenuDAO.save(toSave);
        }
    }

    private void checkIdInMenu(Integer id, LocalDate date) throws InvalidDishId {
        DailyMenuId dailyMenuId = new DailyMenuId(date, id);
        if (!dailyMenuDAO.existsById(dailyMenuId)){
            throw new InvalidDishId("Invalid id: " + id + ". This id is not present in " + date.toString() + " date menu.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void checkIdsInMenu(Integer[] ids, LocalDate date) throws InvalidDishId {
        for( Integer id : ids) {
            checkIdInMenu(id, date);
        }
    }


}
