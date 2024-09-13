package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    @GetMapping
    public Collection<Genre> getAllGenres() {
        return service.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable @Positive int id) {
        return service.getGenreById(id);
    }
}
