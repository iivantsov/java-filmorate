package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class Genre {
    private Integer id;
    @NotBlank
    private String name;
}
