package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private int newId = 0;

    public Film addFilm(Film film) {
        if (validateFilm(film)) {
            film.setId(++newId);

            return filmStorage.addFilm(film);
        }

        return null;
    }

    public Film updateFilm(Film film) {
        if (validateFilm(film)) {
            return filmStorage.updateFilm(film);
        }

        return null;
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    private boolean validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String textError = "Дата релиза — не раньше 28 декабря 1895 года.";

            log.debug("Валидация не пройдена: " + textError);
            throw new ValidationException(textError);
        }

        return true;
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public Film addLikeToFilm(int id, int userId) {
        Film film = filmStorage.getFilm(id);

        userStorage.getUser(userId); // Проверка существования пользователя
        film.addLikeUserId(userId);

        return filmStorage.updateFilm(film);
    }

    public Film removeLikeToFilm(int id, int userId) {
        Film film = filmStorage.getFilm(id);

        userStorage.getUser(userId);
        film.removeLikeUserId(userId);

        return filmStorage.updateFilm(film);
    }

    public List<Film> findFilmsByLikes(int count) {
        return filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparing(o -> o.getLikeUserIds().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }
}