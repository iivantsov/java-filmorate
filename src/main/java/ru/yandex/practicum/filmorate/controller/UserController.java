package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<User> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User add(@RequestBody @Valid User user) {
        return service.add(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        return service.update(user);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getAllFriends(@PathVariable int id) {
        return service.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable("id") int userId, @PathVariable("otherId") int otherUserId) {
        return service.getCommonFriends(userId, otherUserId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        return service.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        return  service.removeFriend(userId, friendId);
    }
}
