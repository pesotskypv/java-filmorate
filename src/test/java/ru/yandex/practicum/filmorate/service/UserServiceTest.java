package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserServiceTest {

    @Test
    void validateUserTest_shouldReturnNameUser() {
        User user = new UserService(new InMemoryUserStorage(), null)
                .createUser(new User(0, "User", "",
                        LocalDate.of(2000, 12, 28), "mail@yandex.ru"));

        assertFalse(user.getName().isEmpty(), "Отсутствует имя.");
        assertEquals("User", user.getName(), "Неверное имя.");
    }

    @Test
    void validateUserTest_shouldReturnNameUser2() {
        User user = new UserService(new InMemoryUserStorage(), null)
                .createUser(new User(0, "User2", null,
                        LocalDate.of(2000, 12, 28), "mail@yandex.ru"));

        assertFalse(user.getName().isEmpty(), "Отсутствует имя.");
        assertEquals("User2", user.getName(), "Неверное имя.");
    }

    @Test
    void validateUserTest_shouldReturnNameUser3() {
        User user = new UserService(new InMemoryUserStorage(), null)
                .createUser(new User(0, "User", "User3",
                        LocalDate.of(2000, 12, 28), "mail@yandex.ru"));

        assertFalse(user.getName().isEmpty(), "Отсутствует имя.");
        assertEquals("User3", user.getName(), "Неверное имя.");
    }
}