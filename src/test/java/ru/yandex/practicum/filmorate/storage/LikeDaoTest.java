package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDaoTest {
    private final LikeDao likeDao;

    @Test
    @Sql("/test-user-data.sql")
    public void testFindLikeUserIds() {
        Optional<List<Long>> likeOptional = Optional.ofNullable(likeDao.findLikeUserIds(1));

        assertThat(likeOptional)
                .isPresent()
                .hasValueSatisfying(like ->
                        assertThat(like).hasNoNullFieldsOrProperties()
                );
    }

    @Test
    @Sql("/test-user-data.sql")
    public void testFindFilmsByLikes() {
        Optional<List<Film>> likeOptional = Optional.ofNullable(likeDao.findFilmsByLikes(2));

        assertThat(likeOptional)
                .isPresent()
                .hasValueSatisfying(like ->
                        assertThat(like).hasNoNullFieldsOrProperties()
                );
    }
}
