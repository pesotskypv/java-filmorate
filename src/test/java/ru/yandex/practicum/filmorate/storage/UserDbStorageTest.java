package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    @Sql("/test-user-data.sql")
    public void testCreateUser() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.createUser(
                User.builder()
                        .login("friend")
                        .name("friend")
                        .birthday(LocalDate.of(2000, 12, 28))
                        .email("friend@mail.ru")
                        .build()
        ));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "friend")
                );
    }

    @Test
    @Sql("/test-user-data.sql")
    public void testFindAllUsers() {
        Optional<List<User>> userOptional = Optional.ofNullable(userStorage.findAllUsers());
        System.out.println(userOptional);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasNoNullFieldsOrProperties()
                );
    }

    @Test
    @Sql("/test-user-data.sql")
    public void testGetUser() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(2));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "common")
                );
    }
}