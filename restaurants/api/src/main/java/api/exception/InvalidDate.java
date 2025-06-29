package api.exception;

public class InvalidDate extends RuntimeException {
    public InvalidDate(String message) {
        super(message);
    }
}
