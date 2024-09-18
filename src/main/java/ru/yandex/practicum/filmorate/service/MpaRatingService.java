package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.Collection;

@Service
@AllArgsConstructor
public class MpaRatingService {
    private final MpaRatingStorage storage;

    public Collection<MpaRating> getAllMpaRating() {
        return storage.getAllMpaRatings();
    }

    public MpaRating getMpaRatingById(int id) {
        return storage.getMpaRatingById(id).orElseThrow(() -> new NotFoundException(MpaRating.class, id));
    }
}
