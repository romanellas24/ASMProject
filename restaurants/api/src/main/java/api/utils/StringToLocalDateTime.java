package api.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTime {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
}
