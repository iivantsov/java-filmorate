package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User add(@RequestBody @Valid User user) {
        validate(user);
        int id = getNextId();
        user.setId(id);
        log.info("validated and added - {}", user);
        users.put(id, user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        validate(user);
        Integer id = user.getId();
        if (!users.containsKey(id)) {
            String errorMessage = "id not found - " + user;
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        log.info("validated and updated - {}", user);
        users.put(id, user);
        return user;
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("blank name replaced with login - {}", user.getName());
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
