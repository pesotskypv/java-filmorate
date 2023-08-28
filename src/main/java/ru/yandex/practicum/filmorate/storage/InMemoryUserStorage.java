package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        users.put(user.getId(), user);
        log.debug("Создан пользователь: {}", user);

        return user;
    }

    @Override
    public User updateUser(User user) {
        int id = user.getId();

        userCheckInStorage(id);
        users.put(id, user);
        log.debug("Обновлён пользователь: {}", user);

        return user;
    }

    @Override
    public Collection<User> findAllUsers() {
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