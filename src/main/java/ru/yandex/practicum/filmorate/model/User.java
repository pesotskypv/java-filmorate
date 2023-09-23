package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {

    private int id;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S*$") // Регулярное выражение для проверки отсутствия пробелов в строке
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

    @Getter(AccessLevel.NONE)
    private final Set<Long> friends = new HashSet<>();

    public List<Long> getFriendIds() {
        return List.copyOf(friends);
    }

    public void addFriendId(int id) {
        friends.add((long) id);
    }

    public void removeFriendId(int id) {
        friends.remove((long) id);
    }
}