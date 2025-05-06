package asm.couriers.courier_allocation.exception;

public class DeleteRequestTooLateException extends RuntimeException {
    public DeleteRequestTooLateException(String message) {
        super(message);
    }
}
