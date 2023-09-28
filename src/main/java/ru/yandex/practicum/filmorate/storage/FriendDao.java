package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FriendDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Mappers mappers;

    public List<Long> findUserFriendsIds(int id) {
        String query = "SELECT friend_id FROM friends_users WHERE user_id = :id";
        SqlParameterSource namedParams = new MapSqlParameterSource("id", id);

        return jdbcTemplate.queryForList(query, namedParams, Long.class);
    }

    public void addUserFriendId(int id, int friendId) {
        String query =
                "INSERT INTO friends_users (user_id, friend_id) VALUES (:user_id, :friend_id) ON CONFLICT DO NOTHING";
        Map<String, Object> namedParams = Map.of("user_id", id, "friend_id", friendId);

        jdbcTemplate.update(query, new MapSqlParameterSource(namedParams));
        log.debug(String.format("Пользователю user_id %d добавлен друг friend_id %d", id, friendId));
    }

    public void removeUserFriendId(int id, int friendId) {
        String query =
                "DELETE FROM friends_users WHERE user_id = :user_id AND friend_id = :friend_id";
        Map<String, Object> namedParams = Map.of("user_id", id, "friend_id", friendId);

        jdbcTemplate.update(query, new MapSqlParameterSource(namedParams));
        log.debug(String.format("У пользователя user_id %d удалён друг friend_id %d", id, friendId));
    }

    public List<User> findUserFriends(int id) {
        String query =
                "SELECT * FROM users WHERE user_id IN (SELECT friend_id FROM friends_users WHERE user_id = :id)";
        SqlParameterSource namedParams = new MapSqlParameterSource("id", id);

        return jdbcTemplate.query(query, namedParams, mappers.userMapper);
    }

    public List<User> findUserMutualFriends(int id, int otherId) {
        String query = "SELECT * FROM users WHERE user_id IN (" +
                "SELECT f1.friend_id FROM friends_users f1 " +
                "JOIN friends_users f2 ON f1.friend_id = f2.friend_id " +
                "WHERE f1.user_id = :id AND f2.user_id = :other_id)";
        Map<String, Object> namedParams = Map.of("id", id, "other_id", otherId);

        return jdbcTemplate.query(query, namedParams, mappers.userMapper);
    }
}