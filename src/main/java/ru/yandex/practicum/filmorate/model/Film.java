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
    private final Set<Genre> genres = new HashSet<>();

    private Mpa mpa;

    @Getter(AccessLevel.NONE)
    private final Set<Long> likes = new HashSet<>();

    public List<Genre> getGenres() {
        return List.copyOf(genres);
    }

    public void addGenres(List<Genre> list) {
        genres.addAll(list);
    }

    public void addLikeUserId(List<Long> list) {
        likes.addAll(list);
    }
}