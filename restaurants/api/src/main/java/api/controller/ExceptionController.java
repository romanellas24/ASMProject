package api.controller;

import api.dto.ExceptionDTO;
import api.exception.*;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(InvalidDishId.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> invalidDishId(InvalidDishId invalidDishId) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(invalidDishId.getMessage(), 404);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AcmeNotificationException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ExceptionDTO> acmeNotificationException(AcmeNotificationException acmeNotificationException) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(acmeNotificationException.getMessage(), 503);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionDTO> notFoundException(NotFoundException notFoundException) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(notFoundException.getMessage(), 404);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TimeoutException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ResponseEntity<ExceptionDTO> timeoutException(TimeoutException timeoutException) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(timeoutException.getMessage(), 408);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(InvalidDateTimeFormat.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> invalidDateTimeFormat(InvalidDateTimeFormat invalidDateTimeFormat) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(invalidDateTimeFormat.getMessage(), 400);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CompanyIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> invalidCompanyOrderId(CompanyIdException companyIdException) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(companyIdException.getMessage(), 400);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDate.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> invalidDate(InvalidDate companyIdException) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(companyIdException.getMessage(), 400);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionDTO> internalServerException(Exception exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), 500);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
