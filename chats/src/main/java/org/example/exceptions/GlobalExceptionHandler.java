package org.example.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ChatException.class, AuthException.class})
    public ResponseEntity<ErrorResponse> handleCustomExceptions(Exception ex) {
        log.error("Handled exception: ", ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ex instanceof AuthException) status = HttpStatus.FORBIDDEN;

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unhandled exception: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
