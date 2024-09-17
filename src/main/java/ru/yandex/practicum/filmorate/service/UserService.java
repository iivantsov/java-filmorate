package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public Collection<User> getAllUsers() {
        return storage.getAllUsers();
    }

    public User getUserById(int id) {
        return storage.getUserById(id).orElseThrow(() -> new NotFoundException("user with id " + id + " not found"));
    }

    public void validateUserPresenceById(int id) {
        getUserById(id);
    }

    public User addUser(User user) {
        validate(user);
        return storage.addUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        return storage.updateUser(user);
    }

    public Set<User> getAllUserFriends(int id) {
        getUserById(id);
        return storage.getAllUserFriends(id);
    }

    public Set<User> getCommonFriends(int userId, int otherUserId) {
        return storage.getCommonFriends(userId, otherUserId);
    }

    public User addFriend(int userId, int friendId) {
        User friend = getFriend(userId, friendId);
        storage.manageFriendship(userId, friendId, UserStorage.UserFriendManageAction.ADD);
        return friend;
    }

    public User removeFriend(int userId, int friendId) {
        User friend = getFriend(userId, friendId);
        storage.manageFriendship(userId, friendId, UserStorage.UserFriendManageAction.DEL);
        return friend;
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("blank name replaced with login - {}", user.getName());
        }
    }

    private User getFriend(int userId, int friendId) {
        Map<Integer, User> idsToUsers = getAllUsers().stream()
                .filter(user -> user.getId() == userId || user.getId() == friendId)
                .collect(Collectors.toMap(User::getId, Function.identity()));

        if (!idsToUsers.containsKey(userId)) {
            throw new NotFoundException("user with id " + userId + " not found");
        } else if (!idsToUsers.containsKey(friendId)) {
            throw new NotFoundException("user with id " + friendId + " not found");
        }

        return idsToUsers.get(friendId);
    }
}
