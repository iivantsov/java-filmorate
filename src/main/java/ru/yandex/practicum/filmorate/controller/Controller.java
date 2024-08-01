package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Element;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Controller<E extends Element> {
    Map<Integer, E> elements = new HashMap<>();

    @GetMapping
    public Collection<E> get() {
        return elements.values();
    }

    @PostMapping
    public E add(@RequestBody E element) {
        int id = getNextId();
        element.setId(id);
        elements.put(id, element);
        return element;
    }

    @PutMapping
    public E update(@RequestBody E element) {
        Integer id = element.getId();
        if (!elements.containsKey(id)) {
            throw new NotFoundException(element.getClass().getSimpleName() + " with id = " + id + " not found");
        }
        elements.put(id, element);
        return element;
    }

    protected int getNextId() {
        int nextId = elements.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++nextId;
    }
}
