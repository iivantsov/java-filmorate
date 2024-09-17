package ru.yandex.practicum.filmorate.repository.mapper;

import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendshipStatusRowMapper implements RowMapper<FriendshipStatus> {

    @Override
    public FriendshipStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
        FriendshipStatus friendshipStatus = new FriendshipStatus();
        friendshipStatus.setId(rs.getInt("status_id"));
        friendshipStatus.setName(rs.getString("status_name"));
        return friendshipStatus;
    }
}
