package api.controller;

import api.dto.MenuDTO;
import api.dto.UpdateMenuResponseDTO;
import api.exception.InvalidDishId;
import api.service.MenuService;
import api.utils.MenuUpdateDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     *
     * @param date - date format is handled by Params, so no custom exception
     * @return menu
     */
    @GetMapping
    public MenuDTO getMenu(@RequestParam(name = "date", required = false) LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }
        return menuService.getMenu(date);
    }

    /**
     * If the request is made BEFORE 10am will be changed the today menu, otherwise the one of the next day
     * @param dishIds list of dish id to put in menu
     */
    @PutMapping
    public UpdateMenuResponseDTO updateMenu(@RequestBody List<Integer> dishIds) throws InvalidDishId {
        LocalDate date = MenuUpdateDate.get();
        menuService.updateMenu(date, dishIds);
        if (date == LocalDate.now().plusDays(1)) {
            menuService.notifyMenuChanges(date);
        }
        return new UpdateMenuResponseDTO(date);
    }
}
