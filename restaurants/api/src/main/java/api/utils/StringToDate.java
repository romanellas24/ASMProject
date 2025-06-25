package api.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToDate {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDateTime convertStringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, formatter);
    }

    public static boolean isStringValid(String date) {
        try {
            LocalDateTime.parse(date, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static LocalDate convertStringToLocalDate(String date) {
        if (isStringValid(date)) {
            return convertStringToLocalDateTime(date).toLocalDate();
        } else {
            return LocalDate.parse(date, localDateFormatter);
        }
    }

    public static boolean isStringLocalDateValid(String date) {
        try {
            LocalDate.parse(date, localDateFormatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static LocalDateTime getEndOfDay(LocalDate date){
        return date.atTime(LocalTime.MAX);
    }
}
