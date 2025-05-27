package api.service;

import api.dto.MenuDTO;
import api.exception.AcmeNotificationException;
import api.exception.InvalidDishId;

import java.time.LocalDate;
import java.util.List;

public interface MenuService {
    MenuDTO getMenu(LocalDate date);
    void updateMenu(LocalDate date, List<Integer> dishIds) throws InvalidDishId;
    void notifyMenuChanges(LocalDate date) throws AcmeNotificationException;
    void checkIdsInMenu(Integer[] id, LocalDate date) throws InvalidDishId;
}
