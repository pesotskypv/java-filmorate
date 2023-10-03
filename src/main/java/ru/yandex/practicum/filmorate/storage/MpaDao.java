package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.FilmMpa;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MpaDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Mappers mappers;

    public List<Mpa> findAllMpa() {
        String query = "SELECT * FROM mpa";

        return jdbcTemplate.query(query, mappers.mpaMapper);
    }

    public List<FilmMpa> findMpa(List<Integer> ids) {
        String query = "SELECT film_id, mpa_id FROM films WHERE film_id IN (:id)";
        SqlParameterSource namedParams = new MapSqlParameterSource("id", ids);

        return jdbcTemplate.query(query, namedParams, mappers.filmMpaMapper);
    }

    public Mpa getMpa(int id) {
        String textError = String.format("Отсутствует рейтинг с id %d", id);
        String query = "SELECT * FROM mpa WHERE mpa_id = :id";
        Map<String, Object> namedParams = Map.of("id", id);

        try {
            return jdbcTemplate.queryForObject(query, namedParams, mappers.mpaMapper);
        } catch (EmptyResultDataAccessException e) {
            log.debug(textError);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, textError);
        }
    }

    public Mpa getMpaByFilmId(int id) {
        String textError = String.format("Отсутствует ИД рейтинга в фильме id %d", id);
        String query = "SELECT m.* FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "WHERE f.film_id = :id";
        Map<String, Object> namedParams = Map.of("id", id);

        try {
            return jdbcTemplate.queryForObject(query, namedParams, mappers.mpaMapper);
        } catch (EmptyResultDataAccessException e) {
            log.debug(textError);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, textError);
        }
    }
}