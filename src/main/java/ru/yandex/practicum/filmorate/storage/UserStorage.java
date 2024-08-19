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

    Collection<User> getAllUsers();

    Optional<User> getUserById(int id);

    User addUser(User user);

    User updateUser(User user);

    Set<User> getAllUserFriends(int id);

    Set<User> getCommonFriends(int userId, int otherUserId);

    User manageFriend(int userId, int friendId, UserFriendManageAction action);
}
