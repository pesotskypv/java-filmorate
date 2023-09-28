package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendDaoTest {
    private final FriendDao friendDao;

    @Test
    @Sql("/test-user-data.sql")
    public void testFindUserFriendsIds() {
        Optional<List<Long>> friendOptional = Optional.ofNullable(friendDao.findUserFriendsIds(1));

        assertThat(friendOptional)
                .isPresent()
                .hasValueSatisfying(friend ->
                        assertThat(friend).hasNoNullFieldsOrProperties()
                );
    }

    @Test
    @Sql("/test-user-data.sql")
    public void testFindUserFriends() {
        Optional<List<User>> friendOptional = Optional.ofNullable(friendDao.findUserFriends(1));

        assertThat(friendOptional)
                .isPresent()
                .hasValueSatisfying(friend ->
                        assertThat(friend).hasNoNullFieldsOrProperties()
                );
    }

    @Test
    @Sql("/test-user-data.sql")
    public void testFindUserMutualFriends() {
        Optional<List<User>> friendOptional = Optional.ofNullable(friendDao.findUserMutualFriends(1, 3));

        assertThat(friendOptional)
                .isPresent()
                .hasValueSatisfying(friend ->
                        assertThat(friend).hasNoNullFieldsOrProperties()
                );
    }
}