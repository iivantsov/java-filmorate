package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @GetMapping
    public Collection<Film> getAll() {
        return service.getAll();
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        return service.getPopular(count);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable @Positive int id) {
        return service.getById(id);
    }

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
        return service.add(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return service.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        return service.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        return service.removeLike(filmId, userId);
    }
}
