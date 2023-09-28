package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoTest {
    private final GenreDao genreDao;

    @Test
    public void testFindGenres() {
        Optional<List<Genre>> genreOptional = Optional.ofNullable(genreDao.findGenres());

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasNoNullFieldsOrProperties()
                );
    }

    @Test
    public void testGetGenre() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreDao.getGenre(1));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    @Sql("/test-user-data.sql")
    public void testFindFilmGenres() {
        Optional<List<Genre>> genreOptional = Optional.ofNullable(genreDao.findFilmGenres(1));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasNoNullFieldsOrProperties()
                );
    }
}