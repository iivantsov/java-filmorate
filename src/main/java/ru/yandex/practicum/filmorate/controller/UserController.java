package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("filmorate/users")
public class UserController extends Controller<User> {
}
