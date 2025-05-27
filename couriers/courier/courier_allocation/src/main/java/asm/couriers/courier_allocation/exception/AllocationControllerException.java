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
        maps service unavailable
     */
    @ExceptionHandler(MapsServiceException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ExceptionDTO> mapsServiceException(MapsServiceException ex) {
        ExceptionDTO exception = new ExceptionDTO(ex.getMessage(), 503);
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
        ExceptionDTO exception = new ExceptionDTO(ex.getMessage(), 401);
        return new ResponseEntity<>(exception, HttpStatus.UNAUTHORIZED);
    }

    /*
        Handle generic errors (400)
     */

    @ExceptionHandler(DeleteRequestTooLateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> deleteRequestTooLate(Exception ex) {
        ExceptionDTO exception = new ExceptionDTO(ex.getMessage(), 400);
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateTimeFormat.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> invalidDateTimeFormat(Exception ex) {
        ExceptionDTO exception = new ExceptionDTO(ex.getMessage(), 400);
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionDTO> exception(Exception ex) {
        ExceptionDTO exception = new ExceptionDTO(ex.getMessage(), 500);
        return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
