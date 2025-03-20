package asm.couriers.courier_allocation.exception;

import asm.couriers.courier_allocation.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AllocationControllerException {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ExceptionDTO> vehicleNotAvailable(Exception ex) {
        ExceptionDTO exception = new ExceptionDTO(ex.getMessage(), 400);
        return new ResponseEntity<>(exception, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
