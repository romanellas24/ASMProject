package api.exception;

public class InvalidDishId extends RuntimeException {
    public InvalidDishId(String message) {
        super(message);
    }
}
