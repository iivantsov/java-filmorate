package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public Collection<User> getAll() {
        return storage.getAll();
    }

    public User getById(int id) {
        return storage.getById(id).orElseThrow(() -> new NotFoundException("user with id " + id + " not found"));
    }

    public User add(User user) {
        validate(user);
        return storage.add(user);
    }

    public User update(User user) {
        validate(user);
        return storage.update(user);
    }

    public Set<User> getAllFriends(int id) {
        try {
            return storage.getAllFriends(id);
        } catch (NullPointerException e) {
            throw new NotFoundException("user with id " + id + " not found");
        }
    }

    public Set<User> getCommonFriends(int userId, int otherUserId) {
        return storage.getCommonFriends(userId, otherUserId);
    }

    public User addFriend(int userId, int friendId) {
        return storage.manageFriend(userId, friendId, UserStorage.UserFriendManageAction.ADD);
    }

    public User removeFriend(int userId, int friendId) {
        return storage.manageFriend(userId, friendId, UserStorage.UserFriendManageAction.REMOVE);
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("blank name replaced with login - {}", user.getName());
        }
    }
}
