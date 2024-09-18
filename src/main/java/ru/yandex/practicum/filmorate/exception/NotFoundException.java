package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> type, Object id) {
        super(type + " with id " + id + " not found");
    }
}
