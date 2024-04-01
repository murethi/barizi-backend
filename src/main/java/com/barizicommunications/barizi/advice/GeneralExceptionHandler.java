package com.barizicommunications.barizi.advice;

import com.barizicommunications.barizi.dto.response.ExceptionResponse;
import com.barizicommunications.barizi.exceptions.InvalidQuantityException;
import com.barizicommunications.barizi.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ExceptionResponse handleNotFound(HttpServletRequest req, NotFoundException exception) {
        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(value = InvalidQuantityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionResponse handleInvalidQuantity(HttpServletRequest req, InvalidQuantityException exception) {
        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
