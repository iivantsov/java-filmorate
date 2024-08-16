package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public Optional<User> getById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User add(User user) {
        int id = getNextId();
        user.setId(id);
        users.put(id, user);
        log.info("validated and added - {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        Integer id = user.getId();
        if (!users.containsKey(id)) {
            String errorMessage = "id not found - " + user;
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        users.put(id, user);
        log.info("validated and updated - {}", user);
        return user;
    }

    public int getNextId() {
        int nextId = users.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++nextId;
    }
}
