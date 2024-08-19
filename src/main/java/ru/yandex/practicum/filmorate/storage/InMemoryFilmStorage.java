package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film addFilm(Film film) {
        int id = getNextId();
        film.setId(id);
        films.put(id, film);
        log.info("validated and added - {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Integer id = film.getId();
        if (!films.containsKey(id)) {
            throw new NotFoundException("film with id " + id + " not found");
        }
        films.put(id, film);
        log.info("validated and updated - {}", film);
        return film;
    }

    @Override
    public Film manageLike(int filmId, int userId, LikeManageAction action) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("film with id " + filmId + " not found");
        }
        Film film = films.get(filmId);

        switch (action) {
            case ADD -> {
                film.getUsersWhoLiked().add(userId);
                log.info("user id {} liked film id {}", userId, filmId);
                log.debug("{}", film);
            }
            case REMOVE -> {
                film.getUsersWhoLiked().remove(userId);
                log.info("user id {} unliked film id {}", userId, filmId);
                log.debug("{}", film);
            }
        }
        return film;
    }

    public int getNextId() {
        int nextId = films.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++nextId;
    }
}
