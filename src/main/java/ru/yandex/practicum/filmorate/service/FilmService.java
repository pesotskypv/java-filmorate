package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FilmService {
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage, GenreDao genreDao, MpaDao mpaDao,
                       LikeDao likeDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
        this.likeDao = likeDao;
    }

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreDao genreDao;
    private final MpaDao mpaDao;
    private final LikeDao likeDao;

    public Film addFilm(Film film) {
        validateFilm(film);
        List<Genre> genres = film.getGenres();

        for (Genre genre : genres) { // Проверка существования жанра
            genreDao.getGenre(genre.getId());
        }

        Film filmUpd = filmStorage.addFilm(film);
        int id = filmUpd.getId();

        for (Genre genre : genres) {
            genreDao.addFilmGenre(id, genre.getId());
        }

        Film filmNew = getFilm(id);
        log.debug("Добавлен фильм: {}", filmNew);
        return filmNew;
    }

    public Film updateFilm(Film film) {
        int id = film.getId();

        validateFilm(film);
        filmStorage.getFilm(id); // Проверка существования фильма
        List<Genre> genres = film.getGenres();

        for (Genre genre : genres) { // Проверка существования жанра
            genreDao.getGenre(genre.getId());
        }
        filmStorage.updateFilm(film);
        genreDao.removeAllFilmGenre(id);
        for (Genre genre : genres) {
            genreDao.addFilmGenre(id, genre.getId());
        }

        Film filmUpd = getFilm(id);
        log.debug("Обновлён фильм: {}", filmUpd);

        return filmUpd;
    }

    public List<Film> findAllFilms() {
        List<Film> films = filmStorage.findAllFilms();
        for (Film film : films) {
            int id = film.getId();
            film.addGenres(genreDao.findFilmGenres(id));
            film.setMpa(mpaDao.getMpaByFilmId(id));
        }

        return films;
    }

    public Film getFilm(int id) {
        Film film = filmStorage.getFilm(id);
        film.addGenres(genreDao.findFilmGenres(id));
        film.setMpa(mpaDao.getMpaByFilmId(id));
        film.addLikeUserId(likeDao.findLikeUserIds(id));

        return film;
    }

    public Film addLikeToFilm(int id, int userId) {
        filmStorage.getFilm(id); // Проверка существования фильма
        userStorage.getUser(userId); // Проверка существования пользователя
        likeDao.addLikeToFilm(id, userId);

        return getFilm(id);
    }

    public Film removeLikeToFilm(int id, int userId) {
        filmStorage.getFilm(id);  // Проверка существования фильма
        userStorage.getUser(userId); // Проверка существования пользователя
        likeDao.removeLikeToFilm(id, userId);

        return getFilm(id);
    }

    public List<Film> findFilmsByLikes(int count) {
        List<Film> popularFilms = likeDao.findFilmsByLikes(count);
        if (popularFilms.isEmpty()) {
            popularFilms = findAllFilms();
        }

        return popularFilms;
    }

    public List<Genre> findGenres() {
        return genreDao.findGenres();
    }

    public Genre getGenre(int id) {
        return genreDao.getGenre(id);
    }

    public List<Mpa> findMpa() {
        return mpaDao.findMpa();
    }

    public Mpa getMpa(int id) {
        return mpaDao.getMpa(id);
    };

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String textError = "Дата релиза — не раньше 28 декабря 1895 года.";

            log.debug("Валидация не пройдена: " + textError);
            throw new ValidationException(textError);
        }
    }
}