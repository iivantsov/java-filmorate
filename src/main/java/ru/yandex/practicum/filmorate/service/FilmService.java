package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

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
        return List.of();
    }

    public Film getFilmById(int id) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("film with id " + id + " not found"));

        LinkedHashSet<GenreDto> genres = genreStorage.getGenresByFilmId((film.getId())).stream()
                .map(genre -> new GenreDto(genre.getId()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

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
        userService.validateUserPresenceById(userId);
        return filmStorage.manageLike(filmId, userId, FilmStorage.LikeManageAction.ADD);
    }

    public Film unlikeFilm(int filmId, int userId) {
        userService.validateUserPresenceById(userId);
        return filmStorage.manageLike(filmId, userId, FilmStorage.LikeManageAction.REMOVE);
    }
}
