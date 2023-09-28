package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    @Test
    @Sql("/test-user-data.sql")
    public void testAddFilm() {
        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.addFilm(
                Film.builder()
                        .name("nisi eiusmod")
                        .description("adipisicing")
                        .releaseDate(LocalDate.of(2000, 12, 28))
                        .duration(100)
                        .mpa(Mpa.builder().id(1).name("G").build())
                        .build()
        ));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "adipisicing")
                );
    }

    @Test
    @Sql("/test-user-data.sql")
    public void testFindAllFilms() {
        Optional<List<Film>> filmOptional = Optional.ofNullable(filmDbStorage.findAllFilms());
        System.out.println(filmOptional);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasNoNullFieldsOrProperties()
                );
    }

    @Test
    @Sql("/test-user-data.sql")
    public void testGetFilm() {
        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.getFilm(2));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "film2")
                );
    }

}