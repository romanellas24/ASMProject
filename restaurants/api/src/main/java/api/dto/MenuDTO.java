package api.dto;

import api.entity.DailyMenu;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MenuDTO {
    private DishDTO[] dishes;
    private LocalDate date;

    public static MenuDTO from(List<DailyMenu> dailyMenus, LocalDate date) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setDate(date);

        DishDTO[] dishes = new DishDTO[dailyMenus.size()];
        for (int i = 0; i < dailyMenus.size(); i++) {
            DailyMenu dailyMenu = dailyMenus.get(i);
            dishes[i] = DishDTO.from(dailyMenu.getDish());
        }
        menuDTO.setDishes(dishes);
        return menuDTO;
    }

}
