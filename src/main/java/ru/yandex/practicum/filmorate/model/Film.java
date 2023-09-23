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
public class Film {

    private int id;

    @NotNull
    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;

    @Getter(AccessLevel.NONE)
    private final Set<Long> likes = new HashSet<>();

    public List<Long> getLikeUserIds() {
        return List.copyOf(likes);
    }

    public void addLikeUserId(int id) {
        likes.add((long) id);
    }

    public void removeLikeUserId(int id) {
        likes.remove((long) id);
    }
}