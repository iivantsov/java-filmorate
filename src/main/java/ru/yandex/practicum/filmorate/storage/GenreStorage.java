package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    Collection<Genre> getAllGenres();

    Optional<Genre> getGenreById(int id);

    List<Genre> getGenresByFilmId(int filmId);

    void addGenresToFilms(Collection<Film> films);
}
