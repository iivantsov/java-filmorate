package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreRepository extends EntityRepository<Genre> implements GenreStorage {
    private static final String SQL_GET_ALL_GENRES = """
            SELECT *
            FROM genres;
            """;
    private static final String SQL_GET_GENRE_BY_ID = """
            SELECT *
            FROM genres
            WHERE genre_id = ?;
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
}
