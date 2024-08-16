package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film add(Film film) {
        int id = getNextId();
        film.setId(id);
        films.put(id, film);
        log.info("validated and added - {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Integer id = film.getId();
        if (!films.containsKey(id)) {
            String errorMessage = "id not found - " + film;
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        films.put(id, film);
        log.info("validated and updated - {}", film);
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
