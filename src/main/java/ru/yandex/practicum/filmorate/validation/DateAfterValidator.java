package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateAfterValidator implements ConstraintValidator<DateAfter, LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private LocalDate dateConstraint;

    @Override
    public void initialize(DateAfter constraintAnnotation) {
        dateConstraint = LocalDate.parse(constraintAnnotation.date(), FORMATTER);
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date == null || date.isAfter(dateConstraint);
    }
}
