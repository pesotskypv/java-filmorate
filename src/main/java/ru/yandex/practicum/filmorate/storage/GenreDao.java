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
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@RequiredArgsConstructor
public class GenreDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Mappers mappers;

    public List<Genre> findGenres() {
        String query = "SELECT * FROM genres";

        return jdbcTemplate.query(query, mappers.genreMapper);
    }

    public Genre getGenre(int id) {
        String textError = String.format("Отсутствует жанр с id %d", id);
        String query = "SELECT * FROM genres WHERE genre_id = :id";
        Map<String, Object> namedParams = Map.of("id", id);

        try {
            return jdbcTemplate.queryForObject(query, namedParams, mappers.genreMapper);
        } catch (EmptyResultDataAccessException e) {
            log.debug(textError);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, textError);
        }
    }

    public void removeAllFilmGenre(int id) {
        String query = "DELETE FROM genres_films WHERE film_id = :film_id";
        Map<String, Object> namedParams = Map.of("film_id", id);

        jdbcTemplate.update(query, new MapSqlParameterSource(namedParams));
        log.debug(String.format("Удалена записи в genres_films для film_id %d", id));
    }

    public void addFilmGenre(int filmId, int genreId) {
        String query =
                "INSERT INTO genres_films (film_id, genre_id) VALUES (:film_id, :genre_id) ON CONFLICT DO NOTHING";
        Map<String, Object> namedParams = Map.of("film_id", filmId, "genre_id", genreId);

        jdbcTemplate.update(query, new MapSqlParameterSource(namedParams));
        log.debug(String.format("Добавлена запись в genres_films для film_id %d, genre_id %d", filmId, genreId));
    }

    public List<Genre> findFilmGenres(int id) {
        String query =
                "SELECT * FROM genres WHERE genre_id IN (SELECT genre_id FROM genres_films WHERE film_id = :id)";
        SqlParameterSource namedParams = new MapSqlParameterSource("id", id);

        return jdbcTemplate.query(query, namedParams, mappers.genreMapper);
    }
}