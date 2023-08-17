package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ControllerException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
public class FilmRepository {

    private final Map<Integer, Film> films = new HashMap<>();

    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        log.debug("Добавлен фильм: {}", film);

        return film;
    }

    public Film updateFilm(Film film) {
        int id = film.getId();

        if (!films.containsKey(id)) {
            String textError = "Отсутствует фильм с id " + id;

            log.debug(textError);
            throw new ControllerException(textError);
        }
        films.put(film.getId(), film);
        log.debug("Обновлён фильм: {}", film);

        return film;
    }

    public Collection<Film> findAllFilms() {
        return List.copyOf(films.values());
    }
}