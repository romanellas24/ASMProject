package api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyMenuId implements Serializable {
    @Column(name = "day")
    private LocalDate day;

    @Column(name = "dish_id")
    private Integer dishId;
}