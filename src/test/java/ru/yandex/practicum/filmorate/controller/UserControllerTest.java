package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void testInit() {
        userController = new UserController();
    }

    @Test
    public void testUserWithBlankNameIsAddedWithNameEqualToLogin() {
        User blankNameUser = new User();
        blankNameUser.setId(1);
        blankNameUser.setEmail("kesha@yandex.ru");
        blankNameUser.setLogin("Kesha");
        blankNameUser.setName(" ");
        blankNameUser.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));

        User user = userController.add(blankNameUser);

        assertFalse(user.getName().isBlank());
        assertEquals(user.getName(), user.getLogin());
    }
}
