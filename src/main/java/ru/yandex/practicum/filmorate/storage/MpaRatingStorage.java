package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Optional;

public interface MpaRatingStorage {

    Collection<MpaRating> getAllMpaRatings();

    Optional<MpaRating> getMpaRatingById(int id);
}
