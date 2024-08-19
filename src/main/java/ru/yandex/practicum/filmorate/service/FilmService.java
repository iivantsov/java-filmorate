package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;
    private final UserService userService;

    public Collection<Film> getAllFilms() {
        return storage.getAllFilms();
    }

    public Collection<Film> getPopularFilms(int count) {
        return storage.getAllFilms().stream()
                .sorted((film1, film2) -> Integer.compare(film2.getUsersWhoLiked().size(),
                        film1.getUsersWhoLiked().size()))
                .limit(count)
                .toList();
    }

    public Film getFilmById(int id) {
        return storage.getFilmById(id).orElseThrow(() -> new NotFoundException("film with id " + id + " not found"));
    }

    public Film addFilm(Film film) {
        return storage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }

    public Film likeFilm(int filmId, int userId) {
        userService.getUserById(userId);
        return storage.manageLike(filmId, userId, FilmStorage.LikeManageAction.ADD);
    }

    public Film unlikeFilm(int filmId, int userId) {
        userService.getUserById(userId);
        return storage.manageLike(filmId, userId, FilmStorage.LikeManageAction.REMOVE);
    }
}
