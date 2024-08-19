package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User addUser(User user) {
        int id = getNextId();
        user.setId(id);
        users.put(id, user);
        log.info("validated and added - {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        Integer id = user.getId();
        if (!users.containsKey(id)) {
            throw new NotFoundException("user with id " + id + " not found");
        }
        users.put(id, user);
        log.info("validated and updated - {}", user);
        return user;
    }

    @Override
    public Set<User> getAllUserFriends(int id) {
        return users.get(id).getFriends().stream()
                .map(users::get)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getCommonFriends(int userId, int otherUserId) {
        checkNotFound(userId, otherUserId);
        return Stream.concat(users.get(userId).getFriends().stream(), users.get(otherUserId).getFriends().stream())
                .filter(id -> id != userId && id != otherUserId)
                .map(users::get)
                .collect(Collectors.toSet());
    }

    @Override
    public User manageFriend(int userId, int friendId, UserFriendManageAction action) {
        checkNotFound(userId, friendId);
        User user = users.get(userId);
        User friend = users.get(friendId);

        switch (action) {
            case ADD -> {
                user.getFriends().add(friendId);
                friend.getFriends().add(userId);
                log.info("friending - users id {},{}", user.getId(), friend.getId());
                log.debug("{} {}", user, friend);
            }
            case REMOVE -> {
                user.getFriends().remove(friendId);
                friend.getFriends().remove(userId);
                log.info("unfriending - users id {},{}", user.getId(), friend.getId());
                log.debug("{} {}", user, friend);
            }
        }
        return user;
    }

    private void checkNotFound(int userId, int otherUserId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("user with id " + userId + " not found");
        }
        if (!users.containsKey(otherUserId)) {
            throw new NotFoundException("user with id " + otherUserId + " not found");
        }
    }

    private int getNextId() {
        int nextId = users.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++nextId;
    }
}
