package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {

    private int id;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S*$") // Регулярное выражение для проверки отсутствия пробелов в строке
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @Getter(AccessLevel.NONE)
    private final Set<Long> friends = new HashSet<>();

    public List<Long> getFriendIds() {
        return List.copyOf(friends);
    }

    public void addFriendIds(List<Long> list) {
        friends.addAll(list);
    }
}