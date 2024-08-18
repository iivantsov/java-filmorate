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

    public Collection<Film> getAll() {
        return storage.getAll();
    }

    public Collection<Film> getPopular(int count) {
        return storage.getAll().stream()
                .sorted((film1, film2) -> Integer.compare(film2.getUsersWhoLiked().size(),
                        film1.getUsersWhoLiked().size()))
                .limit(count)
                .toList();
    }

    public Film getById(int id) {
        return storage.getById(id).orElseThrow(() -> new NotFoundException("film with id " + id + " not found"));
    }

    public Film add(Film film) {
        return storage.add(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public Film addLike(int filmId, int userId) {
        userService.getById(userId);
        return storage.manageLike(filmId, userId, FilmStorage.LikeManageAction.ADD);
    }

    public Film removeLike(int filmId, int userId) {
        userService.getById(userId);
        return storage.manageLike(filmId, userId, FilmStorage.LikeManageAction.REMOVE);
    }
}
