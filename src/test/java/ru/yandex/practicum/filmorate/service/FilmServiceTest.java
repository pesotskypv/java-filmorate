package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmServiceTest {

    @Test
    void validateFilmTest_shouldThrowValidationException() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> new FilmService().addFilm(new Film(0, "Film", "film",
                        LocalDate.of(1895, 12, 27),60))
        );

        assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", exception.getMessage(),
                "Неверный текст исключения.");
    }

    @Test
    void validateFilmTest_shouldReturnReleaseDate() {
        Film film = new FilmService().addFilm(new Film(0, "Film", "film",
                LocalDate.of(1895, 12, 28),60));

        assertEquals(LocalDate.of(1895, 12, 28), film.getReleaseDate(), "Неверная дата релиза.");
    }
}