package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Element;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

public abstract class Controller<E extends Element> {
    protected final Map<Integer, E> elements = new HashMap<>();
    protected final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @GetMapping
    public Collection<E> get() {
        return elements.values();
    }

    @PostMapping
    public E add(@RequestBody @Valid E element) {
        validate(element);
        int id = getNextId();
        element.setId(id);
        log.info("Validated and added - {}", element);
        elements.put(id, element);
        return element;
    }

    @PutMapping
    public E update(@RequestBody @Valid E element) {
        validate(element);
        Integer id = element.getId();
        if (!elements.containsKey(id)) {
            String errorMessage = element.getClass().getSimpleName() + " with id = " + id + " not found";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        log.info("Validated and updated - {}", element);
        elements.put(id, element);
        return element;
    }

    protected abstract void validate(E element);

    protected int getNextId() {
        int nextId = elements.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++nextId;
    }
}
