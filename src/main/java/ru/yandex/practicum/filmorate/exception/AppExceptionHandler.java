package ru.yandex.practicum.filmorate.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ValidationExceptionResponse> requestBodyExceptionHandler(MethodArgumentNotValidException exception) {
        List<ValidationExceptionResponse> response = exception.getBindingResult().getFieldErrors().stream()
                .map(er -> new ValidationExceptionResponse(er.getObjectName(), er.getField(), er.getDefaultMessage()))
                .toList();
        log.error("request body validation exception - {}", response);
        return response;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse requestParametersExceptionHandler(ConstraintViolationException exception) {
        String userMessage = exception.getConstraintViolations().stream()
                .map(cv -> {
                    String[] parameterParts = cv.getPropertyPath().toString().split("\\.");
                    String parameterName = parameterParts[parameterParts.length - 1];
                    return parameterName + " " + cv.getMessage();
                })
                .collect(Collectors.joining(","));
        log.error("request parameters validation exception - {}", exception.getMessage());
        return new ExceptionResponse(userMessage);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundExceptionHandler(NotFoundException exception) {
        log.error("not found exception - {} ({})", exception.getMessage(), exception.getStackTrace()[0].toString());
        return new ExceptionResponse(exception.getMessage());
    }
}
