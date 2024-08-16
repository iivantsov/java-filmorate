package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private UserController controller;

    @BeforeEach
    public void testInit() {
        UserStorage storage = new InMemoryUserStorage();
        UserService service = new UserService(storage);
        controller = new UserController(service);
    }

    @Test
    public void testUserWithBlankNameIsAddedWithNameEqualToLogin() {
        User blankNameUser = new User();
        blankNameUser.setEmail("kesha@yandex.ru");
        blankNameUser.setLogin("Kesha");
        blankNameUser.setName(" ");
        blankNameUser.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));

        User user = controller.add(blankNameUser);

        assertFalse(user.getName().isBlank());
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void testGetUserByInvalidIdThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> controller.getById(-1));
    }
}
