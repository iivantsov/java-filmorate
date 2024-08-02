package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {

    @Override
    protected void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Blank name replaced with login - {}", user.getName());
        }
    }
}
