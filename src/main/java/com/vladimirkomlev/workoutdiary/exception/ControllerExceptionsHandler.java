package com.vladimirkomlev.workoutdiary.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import static com.vladimirkomlev.workoutdiary.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Order(1)
public class ControllerExceptionsHandler {

    private static Logger logger = LoggerFactory.getLogger(ControllerExceptionsHandler.class);

    @ExceptionHandler
    public ResponseEntity<?> handle(BadCredentialsException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(new Error<>(INVALID_CREDENTIALS, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handle(JwtAuthenticationException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(new Error<>(UNAUTHENTICATED, exception.getMessage()));
    }

    @ExceptionHandler({AccessDeniedException.class, InvalidTokenException.class})
    public ResponseEntity<?> handle(Exception exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(FORBIDDEN).body(new Error<>(ACCESS_DENIED, exception.getClass().getName()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handle(ConstraintViolationException exception) {
        String error = exception.getSQLException().getMessage().split("Подробности: ")[1];
        return ResponseEntity.status(CONFLICT).body(new Error<>(INVALID_CONTENT, error));
    }

    @ExceptionHandler
    public ResponseEntity<?> handle(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors().stream().map(
                fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()
        ).collect(Collectors.toList());
        return ResponseEntity.status(BAD_REQUEST).body(new Error<>(INVALID_CONTENT, errors));
    }

    @ExceptionHandler
    public ResponseEntity<?> handle(HttpMessageNotReadableException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(new Error<>(INVALID_CONTENT, "Data is invalid"));
    }

}
