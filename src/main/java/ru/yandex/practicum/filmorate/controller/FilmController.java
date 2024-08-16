package ru.yandex.practicum.filmorate.controller;

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

    @GetMapping("/{id}")
    public Film getById(@PathVariable int id) {
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
}
