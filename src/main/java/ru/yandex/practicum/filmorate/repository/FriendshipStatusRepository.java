package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class FriendshipStatusRepository extends EntityRepository<FriendshipStatus>  {
    private static final String SQL_GET_ALL_FRIENDSHIP_STATUS = """
            SELECT *
            FROM friendship_status
            ORDER BY status_id;
            """;
    private static final String SQL_GET_FRIENDSHIP_STATUS_BY_ID = """
            SELECT *
            FROM friendship_status
            WHERE status_id = ?;
            """;

    public FriendshipStatusRepository(JdbcTemplate jdbcT, RowMapper<FriendshipStatus> rowMapper) {
        super(jdbcT, rowMapper);
    }

    public Collection<FriendshipStatus> getAllMpaRatings() {
        return getMultipleEntity(SQL_GET_ALL_FRIENDSHIP_STATUS);
    }

    public Optional<FriendshipStatus> getMpaRatingById(int id) {
        return getSingleEntity(SQL_GET_FRIENDSHIP_STATUS_BY_ID, id);
    }
}
