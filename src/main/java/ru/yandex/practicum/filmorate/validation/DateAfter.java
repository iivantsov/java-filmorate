package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateAfterValidator.class)
public @interface DateAfter {
    String date();
    String message() default "date should be after the specified one";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
