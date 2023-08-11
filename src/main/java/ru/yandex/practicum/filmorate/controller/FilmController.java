package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ControllerException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int newId = 0;
    private String textError;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(++newId);
        films.put(film.getId(), film);
        log.debug("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        int id = film.getId();

        if (!films.containsKey(id)) {
            textError = "Отсутствует фильм с id " + id;

            log.debug(textError);
            throw new ControllerException(textError);
        }
        validateFilm(film);
        films.put(id, film);
        log.debug("Обновлён фильм: {}", film);

        return film;
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            textError = "Дата релиза — не раньше 28 декабря 1895 года.";

            log.debug("Валидация не пройдена: " + textError);
            throw new ValidationException(textError);
        }
    }
}