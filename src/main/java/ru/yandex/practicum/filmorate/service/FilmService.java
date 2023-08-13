package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
public class FilmService {

    FilmRepository filmRepository = new FilmRepository();
    private int newId = 0;

    public Film addFilm(Film film) {
        if (validateFilm(film)) {
            film.setId(++newId);

            return filmRepository.addFilm(film);
        }

        return null;
    }

    public Film updateFilm(Film film) {
        if (validateFilm(film)) {
            return filmRepository.updateFilm(film);
        }

        return null;
    }

    public Collection<Film> findAllFilms() {
        return filmRepository.findAllFilms();
    }

    private boolean validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String textError = "Дата релиза — не раньше 28 декабря 1895 года.";

            log.debug("Валидация не пройдена: " + textError);
            throw new ValidationException(textError);
        }

        return true;
    }
}