package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@Qualifier("FilmDbStorage")
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Mappers mappers;

    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO films (\"name\", \"description\", release_date, duration, mpa_id)" +
                "VALUES (:name, :description, :release_date, :duration, :mpa_id)";
        Map<String, Object> namedParams = Map.of("name", film.getName(), "description", film.getDescription(),
                "release_date", film.getReleaseDate(), "duration", film.getDuration(),
                "mpa_id", film.getMpa().getId());

        jdbcTemplate.update(query, new MapSqlParameterSource(namedParams), keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.debug("Добавлен фильм: {}", film);

        return film;
    }

    @Override
    public void updateFilm(Film film) {
        String query = "UPDATE films SET \"name\" = :name, \"description\" = :description," +
                "release_date = :release_date, duration = :duration, mpa_id = :mpa_id WHERE film_id = :id";
        Map<String, Object> namedParams = Map.of("name", film.getName(), "description", film.getDescription(),
                "release_date", film.getReleaseDate(), "duration", film.getDuration(),
                "mpa_id", film.getMpa().getId(), "id", film.getId());

        jdbcTemplate.update(query, new MapSqlParameterSource(namedParams));
        log.debug("Обновлена запись в БД в таблице films для: {}", film);
    }

    @Override
    public List<Film> findAllFilms() {
        String query = "SELECT * FROM films";

        return jdbcTemplate.query(query, mappers.filmMapper);
    }

    @Override
    public Film getFilm(int id) {
        String textError = String.format("Отсутствует фильм с id %d", id);
        String query = "SELECT * FROM films WHERE film_id = :id";
        Map<String, Object> namedParams = Map.of("id", id);

        try {
            return jdbcTemplate.queryForObject(query, namedParams, mappers.filmMapper);
        } catch (EmptyResultDataAccessException e) {
            log.debug(textError);
            throw new FilmNotFoundException(textError);
        }
    }
}