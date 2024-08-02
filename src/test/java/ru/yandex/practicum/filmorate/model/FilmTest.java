package ru.yandex.practicum.filmorate.model;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.*;

public class FilmTest {
    private final Validator validator;
    private Film film;

    public FilmTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeEach
    public void testInit() {
        // Create a valid Film instance
        film = new Film();
        film.setId(1);
        film.setName("Leon");
        film.setDescription("Blockbuster");
        film.setDuration(Duration.ofMinutes(60));
        film.setReleaseDate(LocalDate.now());
    }

    @AfterEach
    public void checkResult() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }

    @Test
    public void testFilmWithNullNameDoesNotPassValidation() {
        film.setName(null);
    }

    @Test
    public void testFilmWithBlankNameDoesNotPassValidation() {
        film.setName(" ");
    }

    @Test
    public void testFilmWithNullDescriptionDoesNotPassValidation() {
        film.setDescription(null);
    }

    @Test
    public void testFilmWithDescriptionOf201SymbolDoesNotPassValidation() {
        byte[] randomBytes = new byte[Film.MAX_DESCRIPTION_SIZE + 1];
        new Random().nextBytes(randomBytes);
        String invalidDescription = new String(randomBytes, StandardCharsets.US_ASCII);
        film.setDescription(invalidDescription);
    }

    @Test
    public void testFilmWithNullReleaseDateDoesNotPassValidation() {
        film.setReleaseDate(null);
    }

    @Test
    public void testFilmWithReleaseDateBefore28Dec1985DoesNotPassValidation() {
        film.setReleaseDate(LocalDate.of(1890, Month.DECEMBER, 28));
    }

    @Test
    public void testFilmWithNullDurationDoesNotPassValidation() {
        film.setDuration(null);
    }

    @Test
    public void testFilmWithNegativeDurationDoesNotPassValidation() {
        film.setDuration(Duration.ZERO.minusSeconds(1));
    }
}
