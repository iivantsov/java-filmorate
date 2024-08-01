package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("filmorate/films")
public class FilmController extends Controller<Film> {
}