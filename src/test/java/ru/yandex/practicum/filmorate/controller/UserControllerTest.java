package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserControllerTest {

    @Test
    void validateUserTest_shouldReturnNameUser() {
        User user = new UserController().createUser(new User(0, "mail@yandex.ru", "User", "",
                LocalDate.of(2000, 12, 28)));

        assertFalse(user.getName().isEmpty(), "Отсутствует имя.");
        assertEquals("User", user.getName(), "Неверное имя.");
    }

    @Test
    void validateUserTest_shouldReturnNameUser2() {
        User user = new UserController().createUser(new User(0, "mail@yandex.ru", "User2", null,
                LocalDate.of(2000, 12, 28)));

        assertFalse(user.getName().isEmpty(), "Отсутствует имя.");
        assertEquals("User2", user.getName(), "Неверное имя.");
    }

    @Test
    void validateUserTest_shouldReturnNameUser3() {
        User user = new UserController().createUser(new User(0, "mail@yandex.ru", "User", "User3",
                LocalDate.of(2000, 12, 28)));

        assertFalse(user.getName().isEmpty(), "Отсутствует имя.");
        assertEquals("User3", user.getName(), "Неверное имя.");
    }
}