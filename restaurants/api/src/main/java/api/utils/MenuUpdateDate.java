package api.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MenuUpdateDate {
    public static LocalDate get() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenAM = now.toLocalDate().atTime(LocalTime.of(10, 0));
        if(now.isBefore(tenAM)) {
            return LocalDate.now();
        } else {
            return LocalDate.now().plusDays(1);
        }
    }
}
