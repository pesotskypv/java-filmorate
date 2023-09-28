package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
@Qualifier("InMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        log.debug("Добавлен фильм: {}", film);

        return film;
    }

    @Override
    public void updateFilm(Film film) {
        int id = film.getId();

        filmCheckInStorage(id);
        films.put(film.getId(), film);
        log.debug("Обновлён фильм: {}", film);
    }

    @Override
    public List<Film> findAllFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public Film getFilm(int id) {
        filmCheckInStorage(id);

        return films.get(id);
    }

    private void filmCheckInStorage(int id) {
        if (!films.containsKey(id)) {
            String textError = String.format("Отсутствует фильм с id %d", id);

            log.debug(textError);
            throw new FilmNotFoundException(textError);
        }
    }
}