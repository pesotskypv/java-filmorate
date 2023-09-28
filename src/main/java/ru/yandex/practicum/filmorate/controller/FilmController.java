package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@Positive @PathVariable int id) {
        return filmService.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLikeToFilm(@Positive @PathVariable int id, @Positive @PathVariable int userId) {
        return filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLikeToFilm(@Positive @PathVariable int id, @Positive @PathVariable int userId) {
        return filmService.removeLikeToFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> findFilmsByLikes(@Positive @RequestParam(required = false, defaultValue = "10") int count) {
        return filmService.findFilmsByLikes(count);
    }

    @GetMapping("/genres")
    public List<Genre> findGenres() {
        return filmService.findGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@Positive @PathVariable int id) {
        return filmService.getGenre(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> findMpas() {
        return filmService.findMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpa(@Positive @PathVariable int id) {
        return filmService.getMpa(id);
    }
}