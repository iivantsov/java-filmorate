package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.validation.WithoutSpaces;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;

    @NotBlank
    @WithoutSpaces
    private String login;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();
}
