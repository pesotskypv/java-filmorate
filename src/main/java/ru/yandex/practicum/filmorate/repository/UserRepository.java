package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ControllerException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserRepository {

    private final Map<Integer, User> users = new HashMap<>();

    public User createUser(User user) {
        users.put(user.getId(), user);
        log.debug("Создан пользователь: {}", user);

        return user;
    }

    public User updateUser(User user) {
        int id = user.getId();

        if (!users.containsKey(id)) {
            String textError = "Отсутствует пользователь с id " + id;

            log.debug(textError);
            throw new ControllerException(textError);
        }
        users.put(id, user);
        log.debug("Обновлён пользователь: {}", user);

        return user;
    }

    public Collection<User> findAllUsers() {
        return List.copyOf(users.values());
    }
}