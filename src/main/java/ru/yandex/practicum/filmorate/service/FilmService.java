package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        filmStorage.addFilm(film);
        addFilmGenres(film);

        return getFilm(film.getId());
    }

    public Film updateFilm(Film film) {
        int id = film.getId();

        validateFilm(film);
        filmStorage.getFilm(id); // Проверка существования фильма
        filmStorage.updateFilm(film);
        genreDao.removeAllFilmGenre(id);
        addFilmGenres(film);

        return getFilm(id);
    }

    public List<Film> findAllFilms() {
        return collectFilmGenresMpa(filmStorage.findAllFilms());
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
        return genreDao.findAllGenres();
    }

    public Genre getGenre(int id) {
        return genreDao.getGenre(id);
    }

    public List<Mpa> findMpa() {
        return mpaDao.findAllMpa();
    }

    public Mpa getMpa(int id) {
        return mpaDao.getMpa(id);
    }

    private void addFilmGenres(Film film) {
        List<Genre> genres = film.getGenres();
        List<FilmGenre> filmGenres = new ArrayList<>();

        if (genres != null) {
            for (Genre genre : genres) {
                filmGenres.add(FilmGenre.builder().filmId(film.getId()).genreId(genre.getId()).build());
            }
            genreDao.addFilmGenre(filmGenres);
        }
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String textError = "Дата релиза — не раньше 28 декабря 1895 года.";

            log.debug("Валидация не пройдена: " + textError);
            throw new ValidationException(textError);
        }
    }

    private List<Film> collectFilmGenresMpa(List<Film> films) {
        Map<Integer, List<Genre>> filmGenres = new HashMap<>();
        Map<Integer, Mpa> filmMpa = new HashMap<>();

        if (films.isEmpty()) {
            return films;
        }

        Map<Integer, Genre> genresAll = genreDao.findAllGenres().stream().collect(Collectors
                .toMap(Genre::getId, Function.identity()));
        Map<Integer, Mpa> mpaAll = mpaDao.findAllMpa().stream().collect(Collectors
                .toMap(Mpa::getId, Function.identity()));
        List<Integer> filmIds = films.stream().map(Film::getId).collect(Collectors.toList());

        for (FilmGenre filmGenre : genreDao.findGenres(filmIds)) {
            int filmId = filmGenre.getFilmId();
            List<Genre> genres;

            if (filmGenres.containsKey(filmId)) {
                genres = filmGenres.get(filmId);
            } else {
                genres = new ArrayList<>();
            }
            genres.add(genresAll.get(filmGenre.getGenreId()));
            filmGenres.put(filmId, genres);
        }
        for (FilmMpa mpa : mpaDao.findMpa(filmIds)) {
            filmMpa.put(mpa.getFilmId(), mpaAll.get(mpa.getMpaId()));
        }
        for (Film film : films) {
            int filmId = film.getId();

            if (filmGenres.containsKey(filmId)) {
                film.addGenres(filmGenres.get(filmId));
            }
            film.setMpa(filmMpa.get(filmId));
        }

        return films;
    }
}