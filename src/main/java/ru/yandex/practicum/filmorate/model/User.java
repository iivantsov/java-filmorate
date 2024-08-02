package ru.yandex.practicum.filmorate.model;

import lombok.ToString;
import ru.yandex.practicum.filmorate.validation.WithoutSpaces;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends Element {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @WithoutSpaces
    private String login;

    private String name;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}
