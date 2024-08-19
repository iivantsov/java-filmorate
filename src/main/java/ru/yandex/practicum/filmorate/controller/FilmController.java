package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10")  @Positive int count) {
        return service.getPopularFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return service.getFilmById(id);
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return service.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable("id") int filmId, @PathVariable int userId) {
        return service.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film unlikeFilm(@PathVariable("id") int filmId, @PathVariable int userId) {
        return service.unlikeFilm(filmId, userId);
    }
}
