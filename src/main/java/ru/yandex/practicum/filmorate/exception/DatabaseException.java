package ru.yandex.practicum.filmorate.exception;

import org.springframework.dao.DataAccessException;

public class DatabaseException extends DataAccessException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}