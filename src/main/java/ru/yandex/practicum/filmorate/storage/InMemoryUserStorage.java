package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@Qualifier("InMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        users.put(user.getId(), user);
        log.debug("Создан пользователь: {}", user);

        return user;
    }

    @Override
    public void updateUser(User user) {
        int id = user.getId();

        userCheckInStorage(id);
        users.put(id, user);
        log.debug("Обновлён пользователь: {}", user);
    }

    @Override
    public List<User> findAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public User getUser(int id) {
        userCheckInStorage(id);

        return users.get(id);
    }

    private void userCheckInStorage(int id) {
        if (!users.containsKey(id)) {
            String textError = String.format("Отсутствует пользователь с id %d", id);

            log.debug(textError);
            throw new UserNotFoundException(textError);
        }
    }
}