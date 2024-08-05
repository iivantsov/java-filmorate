package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Element {
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
