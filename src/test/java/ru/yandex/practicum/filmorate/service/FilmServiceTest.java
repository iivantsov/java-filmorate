package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {
    private Film film;
    private FilmService filmService;

    private User user;
    private UserService userService;

    @BeforeEach
    public void testInit() {
        // Create a valid Film instance
        film = new Film();
        film.setName("Leon");
        film.setDescription("Blockbuster");
        film.setDuration(Duration.ofMinutes(60));
        film.setReleaseDate(LocalDate.now());

        // Create a valid User instance
        user = new User();
        user.setEmail("kesha@yandex.ru");
        user.setLogin("Kesha");
        user.setName("Innokenty");
        user.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));
    }

    @Test
    public void testAddFilmReturnsFilmViewWithAssignedId() {
        Film addedFilm = filmService.addFilm(film);
        assertEquals(film, addedFilm);
        assertNotNull(film.getId());
    }

    @Test
    public void testGetPopularFilmsWithNegativeCountParameterThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> filmService.getPopularFilms(-1));
    }

    @Test
    public void testLikeFilmByNonExistentUserThrowsNotFoundException() {
        filmService.addFilm(film);
        assertThrows(NotFoundException.class, () -> filmService.likeFilm(film.getId(), 1));
    }

    @Test
    public void testGetPopularFilmsReturnsFilmsCollectionSortedByLikesInDescendingOrder() {
        User anotherUser = new User();
        anotherUser.setEmail("goga@yandex.ru");
        anotherUser.setLogin("Goga");
        anotherUser.setName("Georgy");
        anotherUser.setBirthday(LocalDate.of(2000, Month.JANUARY, 2));

        userService.addUser(user);
        userService.addUser(anotherUser);

        Film anotherFilm = new Film();
        anotherFilm.setName("Dumb & Dumber");
        anotherFilm.setDescription("Comedy");
        anotherFilm.setDuration(Duration.ofMinutes(120));
        anotherFilm.setReleaseDate(LocalDate.now());

        filmService.addFilm(film);
        filmService.addFilm(anotherFilm);

        filmService.likeFilm(film.getId(),user.getId());
        filmService.likeFilm(film.getId(),anotherUser.getId());
        filmService.likeFilm(anotherFilm.getId(), anotherUser.getId());

        Collection<Film> expectedFilms = List.of(film, anotherFilm);

        System.out.printf(expectedFilms.toString());

        assertEquals(expectedFilms, filmService.getPopularFilms(10));
    }
}
