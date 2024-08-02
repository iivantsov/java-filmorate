package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ValidationExceptionResponse> handler(MethodArgumentNotValidException exception) {
        List<ValidationExceptionResponse> response = exception.getBindingResult().getFieldErrors().stream()
                .map(e -> new ValidationExceptionResponse(e.getObjectName(), e.getField(), e.getDefaultMessage()))
                .toList();
        log.error("Validation failed - {}", response);
        return response;
    }
}
