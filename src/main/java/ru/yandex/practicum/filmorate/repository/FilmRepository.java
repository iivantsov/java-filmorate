package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.exception.DatabaseException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FilmRepository extends EntityRepository<Film> implements FilmStorage {
    private static final String SQL_GET_ALL_FILMS = """
            SELECT F.*,
                   R.mpa_name
            FROM films AS F
            JOIN mpa_ratings AS R ON R.mpa_id = F.mpa_id
            """;
    private static final String SQL_GET_FILM_BY_ID = """
            SELECT F.*,
                   R.mpa_name
            FROM films AS F
            JOIN mpa_ratings AS R ON R.mpa_id = F.mpa_id
            WHERE F.film_id = ?
            """;
    private static final String SQL_INSERT_FILM = """
            INSERT INTO films (title, description, release_date, duration, mpa_id)
            VALUES (?,?,?,?,?)
            """;
    private static final String SQL_INSERT_FILM_GENRE = """
            INSERT INTO films_genres (film_id, genre_id)
            VALUES (?,?)
            """;
    private static final String SQL_UPDATE_FILM = """
            UPDATE films
            SET title = ?,
                description = ?,
                release_date = ?,
                duration = ?,
                mpa_id = ?
            WHERE film_id = ?
            """;
    private static final String SQL_DELETE_FILM_GENRE = """
            DELETE FROM films_genres
            WHERE film_id = ?
            """;
    private static final String SQL_ADD_LIKE = """
            INSERT INTO films_users_who_liked (film_id, user_id)
            VALUES (?,?)
            """;
    private static final String SQL_DEL_LIKE = """
            DELETE  FROM films_users_who_liked
            WHERE film_id = ? AND user_id = ?
            """;
    private static final String SQL_GET_POPULAR_FILMS = """
            SELECT F.*,
                   R.mpa_name
            FROM films AS F
            JOIN mpa_ratings AS R ON R.mpa_id = F.mpa_id
            JOIN films_users_who_liked AS L ON L.film_id = F.film_id
            GROUP BY F.film_id
            ORDER BY COUNT(L.user_id) DESC
            LIMIT ?
            """;

    public FilmRepository(JdbcTemplate jdbcT, RowMapper<Film> rowMapper) {
        super(jdbcT, rowMapper);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return getMultipleEntity(SQL_GET_ALL_FILMS);
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return getSingleEntity(SQL_GET_FILM_BY_ID, id);
    }

    @Override
    public Film addFilm(Film film) {
        try {
            Integer id = insertEntity(SQL_INSERT_FILM,
                    film.getName(),
                    film.getDescription(),
                    Date.valueOf(film.getReleaseDate()),
                    film.getDuration().toSeconds(),
                    film.getMpa().getId()).orElseThrow(() -> new DatabaseException("invalid film representation"));
            film.setId(id);
        } catch (DataAccessException exception) {
            throw new DatabaseException("wrong mpa rating id " + film.getMpa().getId(), exception.getCause());
        }
        updateFilmsGenres(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            int rowsUpdated = updateEntity(SQL_UPDATE_FILM,
                    film.getName(),
                    film.getDescription(),
                    Date.valueOf(film.getReleaseDate()),
                    film.getDuration().toSeconds(),
                    film.getMpa().getId(),
                    film.getId());
            if (rowsUpdated == 0) {
                throw new NotFoundException(Film.class, film.getId());
            }
        } catch (DataAccessException exception) {
            throw new DatabaseException("wrong mpa rating id " + film.getMpa().getId(), exception.getCause());
        }

        updateFilmsGenres(film);
        return film;
    }

    @Override
    public void manageLike(int filmId, int userId, LikeManageAction action) {
        String sql = "";
        switch (action) {
            case ADD -> sql = SQL_ADD_LIKE;
            case DEL -> sql = SQL_DEL_LIKE;
        }
        updateEntity(sql, filmId, userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        return getMultipleEntity(SQL_GET_POPULAR_FILMS, count);
    }

    private void updateFilmsGenres(Film film) {
        int filmId = film.getId();
        int rowsUpdated = updateEntity(SQL_DELETE_FILM_GENRE, filmId);
        log.debug("{} row deleted from films_genres table for film id {}", rowsUpdated, filmId);

        List<Integer> genreIds = film.getGenres().stream().map(Genre::getId).toList();
        try {
            jdbcT.batchUpdate(SQL_INSERT_FILM_GENRE, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, filmId);
                    ps.setInt(2, genreIds.get(i));
                }

                @Override
                public int getBatchSize() {
                    return genreIds.size();
                }
            });
        } catch (DataAccessException exception) {
            throw new DatabaseException("wrong genre ids " + film.getGenres(), exception.getCause());
        }
    }
}
