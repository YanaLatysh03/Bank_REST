package com.example.bankcards.exception;

import com.example.bankcards.rs.ErrorRs;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRs> handleGenericException(Exception e) {
        log.error("Handle Exception", e);

        var errorDto = new ErrorRs(
                "Internal server error",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorRs> handleEntityNotFound(EntityNotFoundException e) {
        log.error("Handle EntityNotFoundException", e);

        var errorDto = new ErrorRs(
                "Entity not found",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorRs> handleUsernameNotFound(UsernameNotFoundException e) {
        log.error("Handle UsernameNotFoundException", e);

        var errorDto = new ErrorRs(
                "Unauthenticated",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorRs> handleIllegalState(IllegalStateException e) {
        log.error("Handle IllegalStateException", e);

        var errorDto = new ErrorRs(
                "InvalidState",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }
}
