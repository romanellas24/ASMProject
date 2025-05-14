package api.service.impl;

import api.dao.DailyMenuDAO;
import api.dao.DishDAO;
import api.dto.MenuDTO;
import api.entity.DailyMenu;
import api.entity.DailyMenuId;
import api.exception.AcmeNotificationException;
import api.exception.InvalidDishId;
import api.service.MenuService;
import api.utils.HttpClient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private DailyMenuDAO dailyMenuDAO;

    @Autowired
    private DishDAO dishDAO;

    @Autowired
    private HttpClient httpClient;

    @Override
    public MenuDTO getMenu(LocalDate date) {
        List<DailyMenu> dishes = dailyMenuDAO.findByIdDay(date);
        return MenuDTO.from(dishes, date);
    }

    @Override
    @Transactional(rollbackOn = InvalidDishId.class)
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

    @Override
    public void notifyMenuChanges(LocalDate date) throws AcmeNotificationException {
        MenuDTO menuDTO = this.getMenu(date);

        //this can throw AcmeNotificationException
        httpClient.notifyAcmeMenuChanges(menuDTO);
    }


}
