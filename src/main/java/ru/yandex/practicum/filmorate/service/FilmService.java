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

    public Collection<Film> getAll() {
        return storage.getAll();
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
}
