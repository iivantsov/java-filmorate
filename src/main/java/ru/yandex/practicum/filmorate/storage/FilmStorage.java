package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    enum LikeManageAction {
        ADD,
        REMOVE
    }

    Collection<Film> getAllFilms();

    Optional<Film> getFilmById(int id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film manageLike(int filmId, int userId, LikeManageAction action);
}
