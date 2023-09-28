package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MpaDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Mappers mappers;

    public List<Mpa> findMpa() {
        String query = "SELECT * FROM mpa";

        return jdbcTemplate.query(query, mappers.mpaMapper);
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
        String query = "SELECT * FROM mpa WHERE mpa_id IN (SELECT mpa_id FROM films WHERE film_id = :id)";
        Map<String, Object> namedParams = Map.of("id", id);

        try {
            return jdbcTemplate.queryForObject(query, namedParams, mappers.mpaMapper);
        } catch (EmptyResultDataAccessException e) {
            log.debug(textError);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, textError);
        }
    }
}