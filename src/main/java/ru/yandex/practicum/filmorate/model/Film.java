package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class Film extends Element {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;

    @JsonProperty("duration")
    public long getDurationInMinutes() {
        return duration.toSeconds();
    }
}
