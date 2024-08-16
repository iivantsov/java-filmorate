package ru.yandex.practicum.filmorate.exception;

public record ValidationExceptionResponse(String object, String field, String constraint) { }
