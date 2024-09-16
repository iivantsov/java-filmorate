package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.exception.DatabaseException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Collection;
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
        return Set.of();
    }

    @Override
    public Set<User> getCommonFriends(int userId, int otherUserId) {
        return Set.of();
    }

    @Override
    public User manageFriend(int userId, int friendId, UserFriendManageAction action) {
        return null;
    }
}
