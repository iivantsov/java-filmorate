package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MpaRatingRepository extends EntityRepository<MpaRating> implements MpaRatingStorage {
    private static final String SQL_GET_ALL_MPA_RATINGS = """
            SELECT *
            FROM mpa_ratings
            ORDER BY mpa_id;
            """;
    private static final String SQL_GET_MPA_RATING_BY_ID = """
            SELECT *
            FROM mpa_ratings
            WHERE mpa_id = ?;
            """;

    public MpaRatingRepository(JdbcTemplate jdbcT, RowMapper<MpaRating> rowMapper) {
        super(jdbcT, rowMapper);
    }

    public Collection<MpaRating> getAllMpaRatings() {
        return getMultipleEntity(SQL_GET_ALL_MPA_RATINGS);
    }

    public Optional<MpaRating> getMpaRatingById(int id) {
        return getSingleEntity(SQL_GET_MPA_RATING_BY_ID, id);
    }
}
