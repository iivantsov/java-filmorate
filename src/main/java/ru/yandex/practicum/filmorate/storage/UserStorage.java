package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    enum UserFriendManageAction {
        ADD,
        REMOVE
    }

    Collection<User> getAll();

    Optional<User> getById(int id);

    User add(User user);

    User update(User user);

    Set<User> getAllFriends(int id);

    Set<User> getCommonFriends(int userId, int otherUserId);

    User manageFriend(int userId, int friendId, UserFriendManageAction action);
}
