package api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "daily_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyMenu {

    @EmbeddedId
    private DailyMenuId id;

    @ManyToOne
    @MapsId("dish_id")
    @JoinColumn(name = "dish_id", referencedColumnName = "id")
    private Dish dish;

    public DailyMenu(DailyMenuId id) {
        this.id = id;
    }
}
