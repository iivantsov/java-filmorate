package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WithoutSpacesValidator implements ConstraintValidator<WithoutSpaces, String> {

    @Override
    public boolean isValid(String string, ConstraintValidatorContext context) {
        return string == null || !string.contains(" ");
    }
}
