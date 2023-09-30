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
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@Qualifier("UserDbStorage")
@Slf4j
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Mappers mappers;

    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String query = "INSERT INTO users (\"login\", \"name\", birthday, \"email\")" +
                "VALUES (:login, :name, :birthday, :email)";
        Map<String, Object> namedParams = Map.of("login", user.getLogin(), "name", user.getName(),
                "birthday", user.getBirthday(), "email", user.getEmail());

        jdbcTemplate.update(query, new MapSqlParameterSource(namedParams), keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.debug("Создан пользователь: {}", user);

        return user;
    }

    @Override
    public void updateUser(User user) {
        final String query = "UPDATE users SET \"login\" = :login, \"name\" = :name, birthday = :birthday," +
                "\"email\" = :email WHERE user_id = :id";
        Map<String, Object> namedParams = Map.of("id", user.getId(), "login", user.getLogin(),
                "name", user.getName(), "birthday", user.getBirthday(), "email", user.getEmail(),
                "friends", user.getFriendIds());

        jdbcTemplate.update(query, new MapSqlParameterSource(namedParams));
        log.debug("Обновлён пользователь: {}", user);
    }

    @Override
    public List<User> findAllUsers() {
        String query = "SELECT * FROM users";

        return jdbcTemplate.query(query, mappers.userMapper);
    }

    @Override
    public User getUser(int id) {
        String textError = String.format("Отсутствует пользователь с id %d", id);
        String query = "SELECT * FROM users WHERE user_id = :id";
        Map<String, Object> namedParams = Map.of("id", id);

        try {
            return jdbcTemplate.queryForObject(query, namedParams, mappers.userMapper);
        } catch (EmptyResultDataAccessException e) {
            log.debug(textError);
            throw new UserNotFoundException(textError);
        }
    }
}