package com.main.app.exception;

import com.main.app.exception.custom.APIExceptions;
import com.main.app.payload.response.ErrorResponse;
import com.main.app.payload.response.ValidationErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler(APIExceptions.class)
    public ResponseEntity<ErrorResponse> handleApiException(APIExceptions ex) {
        logger.error("API Exception: {}", ex.getMessage(), ex);

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                ex.getErrorCode() != null ? ex.getErrorCode() : "API_ERROR",
                ex.getStatus() != null ? ex.getStatus().value() : HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        );
        return new ResponseEntity<>(error, ex.getStatus() != null ? ex.getStatus() : HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.warn("Validation failed: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ValidationErrorResponse response = new ValidationErrorResponse(
                "Validation Failed",
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                errors
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unhandled exception", ex);

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
