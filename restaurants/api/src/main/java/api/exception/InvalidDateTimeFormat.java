package api.exception;

public class InvalidDateTimeFormat extends RuntimeException {
    public InvalidDateTimeFormat(String message) {
        super(message);
    }
}
