package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreStorage storage;

    public Collection<Genre> getAllGenres() {
        return storage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        return storage.getGenreById(id).orElseThrow(() -> new NotFoundException("genre with id " + id + " not found"));
    }
}
