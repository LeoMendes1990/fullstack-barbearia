package br.com.dio.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(IllegalArgumentExceptions.class)
    public ResponseEntity<StandarError> illegalArgumentException(IllegalArgumentExceptions e, HttpServletRequest request) {
        StandarError se = new StandarError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(se);
    }

    @ExceptionHandler(ObjectNotFoundExceptions.class)
    public ResponseEntity<StandarError> objectNotFoundExceptions(ObjectNotFoundExceptions e, HttpServletRequest request) {
        StandarError se = new StandarError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(se);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandarError> dataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        StandarError se = new StandarError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(se);
    }
}
