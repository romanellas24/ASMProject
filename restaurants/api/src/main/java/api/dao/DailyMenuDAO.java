package api.dao;

import api.entity.DailyMenu;
import api.entity.DailyMenuId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyMenuDAO extends JpaRepository<DailyMenu, DailyMenuId> {
    List<DailyMenu> findByIdDay(LocalDate day);
    Boolean existsByIdDay(LocalDate day);
    void deleteAllByIdDay(LocalDate day);

}
