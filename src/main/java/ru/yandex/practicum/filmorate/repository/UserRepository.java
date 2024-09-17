package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.exception.DatabaseException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepository extends EntityRepository<User> implements UserStorage {
    private static final String SQL_GET_ALL_USER = """
            SELECT *
            FROM users
            """;
    private static final String SQL_GET_USER_BY_ID = """
            SELECT *
            FROM users
            WHERE user_id = ?
            """;
    private static final String SQL_INSERT_USER = """
            INSERT INTO users (user_name, login, email, birthday)
            VALUES (?,?,?,?)
            """;
    private static final String SQL_UPDATE_USER = """
            UPDATE users
            SET user_name = ?,
                login = ?,
                email = ?,
                birthday = ?
            WHERE user_id = ?
            """;
    private static final String SQL_GET_ALL_USER_FRIENDS = """
            SELECT U.*
            FROM users AS U
            JOIN friendship AS F ON U.user_id = F.user2_id
            WHERE F.user1_id = ?;
            """;
    private static final String SQL_GET_USER_COMMON_FRIENDS = """
            SELECT U.*
            FROM users AS U
            JOIN friendship AS F1 ON U.user_id = F1.user2_id
            JOIN friendship AS F2 ON U.user_id = F2.user2_id
            WHERE F1.user1_id = ? AND F2.user1_id = ?
            """;
    private static final String SQL_ADD_FRIENDSHIP = """
            INSERT INTO friendship (user1_id, user2_id, status_id)
            VALUES (?,?,?)
            """;
    private static final String SQL_DEL_FRIENDSHIP = """
            DELETE FROM friendship
            WHERE user1_id = ? AND user2_id = ?
            """;

    private enum FriendshipStatusValues {
        NOT_FRIENDS,
        UNDECIDED,
        FRIENDS
    }

    public UserRepository(JdbcTemplate jdbcT, RowMapper<User> rowMapper) {
        super(jdbcT, rowMapper);
    }

    @Override
    public Collection<User> getAllUsers() {
        return getMultipleEntity(SQL_GET_ALL_USER);
    }

    @Override
    public Optional<User> getUserById(int id) {
        return getSingleEntity(SQL_GET_USER_BY_ID, id);
    }

    @Override
    public User addUser(User user) {
        Integer id = insertEntity(SQL_INSERT_USER,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Date.valueOf(user.getBirthday()))
                .orElseThrow(() -> new DatabaseException("invalid user representation"));
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        int rowsUpdated = updateEntity(SQL_UPDATE_USER,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        if (rowsUpdated == 0) {
            throw new NotFoundException("user with id " + user.getId() + " not found");
        }
        return user;
    }

    @Override
    public Set<User> getAllUserFriends(int id) {
        return new HashSet<>(getMultipleEntity(SQL_GET_ALL_USER_FRIENDS, id));
    }

    @Override
    public Set<User> getCommonFriends(int userId, int otherUserId) {
        return new HashSet<>(getMultipleEntity(SQL_GET_USER_COMMON_FRIENDS, userId, otherUserId));
    }

    @Override
    public void manageFriendship(int userId, int friendId, UserFriendManageAction action) {
        switch (action) {
            case ADD -> updateEntity(SQL_ADD_FRIENDSHIP, userId, friendId, FriendshipStatusValues.UNDECIDED.ordinal());
            case DEL -> updateEntity(SQL_DEL_FRIENDSHIP, userId, friendId);
        }
    }
}
