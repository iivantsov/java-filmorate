package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    enum LikeManageAction {
        ADD,
        DEL
    }

    Collection<Film> getAllFilms();

    Optional<Film> getFilmById(int id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void manageLike(int filmId, int userId, LikeManageAction action);

    Collection<Film> getPopularFilms(int count);
}
