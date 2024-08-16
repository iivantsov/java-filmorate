package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private FilmController controller;

    @BeforeEach
    public void testInit() {
        FilmStorage storage = new InMemoryFilmStorage();
        FilmService service = new FilmService(storage);
        controller = new FilmController(service);
    }

    @Test
    void testGetFilmByInvalidIdThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> controller.getById(-1));
    }
}
