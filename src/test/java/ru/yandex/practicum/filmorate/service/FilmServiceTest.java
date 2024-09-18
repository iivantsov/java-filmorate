package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRatingRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.repository.mapper.MpaRatingRowMapper;
import ru.yandex.practicum.filmorate.repository.mapper.UserRowMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataAccessException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {
        UserService.class,
        UserRepository.class,
        UserRowMapper.class,
        FilmService.class,
        FilmRepository.class,
        FilmRowMapper.class,
        GenreRepository.class,
        GenreRowMapper.class,
        MpaRatingRepository.class,
        MpaRatingRowMapper.class
})
public class FilmServiceTest {
    private Film film;
    private final FilmService filmService;

    private User user;
    private final UserService userService;

    @BeforeEach
    public void testInit() {
        // Create a valid Film instance
        film = new Film();
        film.setName("Leon");
        film.setDescription("Blockbuster");
        film.setDuration(Duration.ofMinutes(60));
        film.setReleaseDate(LocalDate.now());
        MpaRating mpa = new MpaRating();
        mpa.setId(1);
        mpa.setName("G");
        film.setMpa(mpa);

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
    public void testGetFilmByInvalidIdThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> filmService.getFilmById(-1));
    }

    @Test
    public void testGetPopularFilmsWithNegativeCountParameterThrowsIllegalArgumentException() {
        assertThrows(DataAccessException.class, () -> filmService.getPopularFilms(-1));
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
        MpaRating mpa = new MpaRating();
        mpa.setId(2);
        mpa.setName("PG");
        anotherFilm.setMpa(mpa);

        filmService.addFilm(film);
        filmService.addFilm(anotherFilm);

        filmService.likeFilm(film.getId(), user.getId());
        filmService.likeFilm(film.getId(), anotherUser.getId());
        filmService.likeFilm(anotherFilm.getId(), anotherUser.getId());

        Collection<Film> expectedFilms = List.of(film, anotherFilm);

        System.out.printf(expectedFilms.toString());

        assertEquals(expectedFilms, filmService.getPopularFilms(10));
    }
}
