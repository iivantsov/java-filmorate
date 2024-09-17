package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class GenreRepository extends EntityRepository<Genre> implements GenreStorage {
    private static final String SQL_GET_ALL_GENRES = """
            SELECT *
            FROM genres
            """;
    private static final String SQL_GET_GENRE_BY_ID = """
            SELECT *
            FROM genres
            WHERE genre_id = ?
            """;
    private static final String SQL_GET_GENRE_IDS_BY_FILM_ID = """
            SELECT g.genre_id,
                   g.genre_name
            FROM genres AS g
            JOIN films_genres AS fg ON fg.genre_id = g.genre_id
            WHERE fg.film_id = ?
            """;
    private static final String SQL_GET_ALL_GENRES_TO_ALL_FILMS = """
            SELECT g.genre_id,
                   g.genre_name,
                   fg.film_id
            FROM genres AS g
            JOIN films_genres AS fg ON g.genre_id = fg.genre_id
            WHERE fg.film_id IN
            """;

    public GenreRepository(JdbcTemplate jdbcT, RowMapper<Genre> rowMapper) {
        super(jdbcT, rowMapper);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return getMultipleEntity(SQL_GET_ALL_GENRES);
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        return getSingleEntity(SQL_GET_GENRE_BY_ID, id);
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        return getMultipleEntity(SQL_GET_GENRE_IDS_BY_FILM_ID, filmId);
    }

    @Override
    public void addGenresToFilms(Collection<Film> films) {
        // Construct an array of Film IDs to prepare SQL statement
        var filmIds = films.stream()
                .map(Film::getId)
                .toArray();

        String placeholderSequence = String.join(",", Collections.nCopies(filmIds.length, "?")); // ?,?,..?
        String sql = SQL_GET_ALL_GENRES_TO_ALL_FILMS + "(" + placeholderSequence + ")";

        // Convert Films List to Map to fill Genre IDs
        Map<Integer, Film> idsToFilms = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));

        jdbcT.query(sql, (rs, rowNum) -> {
            Film film = idsToFilms.get(rs.getInt("film_id")); // Get Film from Map by obtained film_id
            // Add obtained Genre to Set inside Film
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("genre_name"));
            film.getGenres().add(genre);
            return film; // Not used
        }, filmIds);
    }
}
