package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
        int id = getNextId();
        film.setId(id);
        log.info("validated and added - {}", film);
        films.put(id, film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        Integer id = film.getId();
        if (!films.containsKey(id)) {
            String errorMessage = "id not found - " + film;
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        log.info("validated and updated - {}", film);
        films.put(id, film);
        return film;
    }

    private int getNextId() {
        int nextId = films.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++nextId;
    }
}
