package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@RequiredArgsConstructor
public class LikeDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Mappers mappers;

    public void addLikeToFilm(int id, int userId) {
        String query =
                "INSERT INTO likes_films (film_id, user_id) VALUES (:film_id, :user_id) ON CONFLICT DO NOTHING";
        Map<String, Object> namedParams = Map.of("film_id", id, "user_id", userId);

        jdbcTemplate.update(query, new MapSqlParameterSource(namedParams));
        log.debug(String.format("Фильму film_id %d добавлен лайк user_id %d", id, userId));
    }

    public void removeLikeToFilm(int id, int userId) {
        String query =
                "DELETE FROM likes_films WHERE film_id = :film_id AND user_id = :user_id";
        Map<String, Object> namedParams = Map.of("film_id", id, "user_id", userId);

        jdbcTemplate.update(query, new MapSqlParameterSource(namedParams));
        log.debug(String.format("У фильма film_id %d удалён лайк user_id %d", id, userId));
    }

    public List<Long> findLikeUserIds(int id) {
        String query = "SELECT user_id FROM likes_films WHERE film_id = :id";
        SqlParameterSource namedParams = new MapSqlParameterSource("id", id);

        return jdbcTemplate.queryForList(query, namedParams, Long.class);
    }

    public List<Film> findFilmsByLikes(int count) {
        String query =
                "SELECT l.film_id, f.\"name\", f.\"description\", f.release_date, f.duration, COUNT(l.user_id)\n" +
                "FROM likes_films l\n" +
                "\tJOIN films f ON l.film_id = f.film_id \n" +
                "GROUP BY l.film_id\n" +
                "ORDER BY COUNT(l.user_id) DESC\n" +
                "LIMIT :count";
        SqlParameterSource namedParams = new MapSqlParameterSource("count", count);

        return jdbcTemplate.query(query, namedParams, mappers.filmMapper);
    }
}