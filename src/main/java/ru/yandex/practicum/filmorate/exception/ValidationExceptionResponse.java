package ru.yandex.practicum.filmorate.exception;

import lombok.*;

@Getter
@AllArgsConstructor
@ToString
public class ValidationExceptionResponse {
    private String object;
    private String field;
    private String constraint;
}
