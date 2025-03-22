package asm.couriers.courier_allocation.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTime {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static LocalDateTime convertStringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, formatter);
    }
}
