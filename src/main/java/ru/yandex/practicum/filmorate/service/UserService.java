package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;

public class UserService {

    UserRepository userRepository = new UserRepository();
    private int newId = 0;

    public User createUser(User user) {
        validateUser(user);
        user.setId(++newId);

        return userRepository.createUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);

        return userRepository.updateUser(user);
    }

    public Collection<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}