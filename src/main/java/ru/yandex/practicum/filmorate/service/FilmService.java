package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final UserService userService;

    public Collection<Film> getAllFilms() {
        Collection<Film> films = filmStorage.getAllFilms();
        genreStorage.addGenresToFilms(films);
        return films;
    }

    public Collection<Film> getPopularFilms(int count) {
        Collection<Film> popularFilms = filmStorage.getPopularFilms(count);
        genreStorage.addGenresToFilms(popularFilms);
        return popularFilms;
    }

    public Film getFilmById(int id) {
        Film film = filmStorage.getFilmById(id).orElseThrow(() -> new NotFoundException(Film.class, id));
        List<Genre> genres = genreStorage.getGenresByFilmId((film.getId()));
        film.getGenres().addAll(genres);
        return film;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film likeFilm(int filmId, int userId) {
        return manageLike(filmId, userId, FilmStorage.LikeManageAction.ADD);
    }

    public Film unlikeFilm(int filmId, int userId) {
        return manageLike(filmId, userId, FilmStorage.LikeManageAction.DEL);
    }

    private Film manageLike(int filmId, int userId, FilmStorage.LikeManageAction action) {
        userService.validateUserPresenceById(userId);
        Film film = getFilmById(filmId);
        filmStorage.manageLike(filmId, userId, action);
        return film;
    }
}