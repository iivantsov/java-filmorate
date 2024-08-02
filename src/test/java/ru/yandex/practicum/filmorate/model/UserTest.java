package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserTest {
    private final Validator validator;
    private User user;

    public UserTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeEach
    public void testInit() {
        // Create a valid User instance
        user = new User();
        user.setId(1);
        user.setEmail("kesha@yandex.ru");
        user.setLogin("Kesha");
        user.setName("Innokenty");
        user.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));
    }

    @AfterEach
    public void checkResult() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void testUserWithNullEmailDoesNotPassValidation() {
        user.setEmail(null);
    }

    @Test
    public void testUserWithBlankEmailDoesNotPassValidation() {
        user.setEmail("");
    }

    @Test
    public void testUserWithInvalidEmailDoesNotPassValidation() {
        user.setEmail("@ yandex?kesha.ru");
    }

    @Test
    public void testUserWithNullLoginDoesNotPassValidation() {
        user.setLogin(null);
    }

    @Test
    public void testUserWithBlankLoginDoesNotPassValidation() {
        user.setLogin("");
    }

    @Test
    public void testUserWithLoginThatContainsSpacesDoesNotPassValidation() {
        user.setLogin("Ke sha");
    }

    @Test
    public void testUserWithNullBirthdayDateDoesNotPassValidation() {
        user.setBirthday(null);
    }

    @Test
    public void testUserWithBirthdayInTheFutureDoesNotPassValidation() {
        user.setBirthday(LocalDate.now().plusDays(1));
    }
}
