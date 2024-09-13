package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

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
@RequestMapping("/mpa")
public class MpaRatingController {
    private final MpaRatingService service;

    @GetMapping
    public Collection<MpaRating> getAllMpaRatings() {
        return service.getAllMpaRating();
    }

    @GetMapping("/{id}")
    public MpaRating getMpaRatingById(@PathVariable @Positive int id) {
        return service.getMpaRatingById(id);
    }
}
