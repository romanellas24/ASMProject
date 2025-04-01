package asm.couriers.courier_allocation.exception;

import asm.couriers.courier_allocation.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AllocationControllerException {


    /*
        Handle vehicle not available to deliver.
        i.e. all vehicle are currently not available.
     */
    @ExceptionHandler(VehicleNotAvailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ExceptionDTO> vehicleNotAvailable(Exception ex) {
        ExceptionDTO exception = new ExceptionDTO(ex.getMessage(), 400);
        return new ResponseEntity<>(exception, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /*
        Handle all 404 error.
        The message in exception contains what entity is not found.
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionDTO> notFound(Exception ex) {
        ExceptionDTO exception = new ExceptionDTO(String.format("%s not found", ex.getMessage()), 404);
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    /*
        Handle Unauthorized (401) error.
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionDTO> unauthorized(Exception ex) {
        ExceptionDTO exception = new ExceptionDTO(ex.getMessage(), 400);
        return new ResponseEntity<>(exception, HttpStatus.UNAUTHORIZED);
    }


}
