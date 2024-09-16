package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.validation.DateAfter;
import ru.yandex.practicum.filmorate.validation.PositiveDuration;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class Film {
    public static final int MAX_DESCRIPTION_SIZE = 200;
    private Integer id;
    @NotBlank
    private String name;

    @NotNull
    @Size(max = MAX_DESCRIPTION_SIZE)
    private String description;

    @NotNull
    @DateAfter(date = "1895-12-28")
    private LocalDate releaseDate;

    @NotNull
    @PositiveDuration
    private Duration duration;

    @JsonProperty("duration")
    public long getDurationInMinutes() {
        return duration.toSeconds();
    }

    private MpaRatingDto mpa;

    @JsonDeserialize(as = LinkedHashSet.class)
    private final Set<GenreDto> genres = new LinkedHashSet<>();
}
