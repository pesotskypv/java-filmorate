package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ControllerException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int newId = 0;
    String textError;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validateUser(user);
        user.setId(++newId);
        users.put(user.getId(), user);
        log.debug("Создан пользователь: {}", user);

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        int id = user.getId();

        if (!users.containsKey(id)) {
            textError = "Отсутствует пользователь с id " + id;

            log.debug(textError);
            throw new ControllerException(textError);
        }
        validateUser(user);
        users.put(id, user);
        log.debug("Обновлён пользователь: {}", user);

        return user;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}