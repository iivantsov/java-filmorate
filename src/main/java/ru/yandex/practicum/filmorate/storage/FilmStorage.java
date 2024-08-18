package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    enum LikeManageAction {
        ADD,
        REMOVE
    }

    Collection<Film> getAll();

    Optional<Film> getById(int id);

    Film add(Film film);

    Film update(Film film);

    Film manageLike(int filmId, int userId, LikeManageAction action);
}
