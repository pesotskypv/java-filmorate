package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    void updateFilm(Film film);

    List<Film> findAllFilms();

    Film getFilm(int id);
}