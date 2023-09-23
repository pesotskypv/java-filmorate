package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private int newId = 0;

    public User createUser(User user) {
        validateUser(user);
        user.setId(++newId);

        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);

        return userStorage.updateUser(user);
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public User addUserFriend(int id, int friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);

        user.addFriendId(friendId);
        friend.addFriendId(id);
        userStorage.updateUser(friend);

        return userStorage.updateUser(user);
    }

    public User removeUserFriend(int id, int friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);

        user.removeFriendId(friendId);
        friend.removeFriendId(id);
        userStorage.updateUser(friend);

        return userStorage.updateUser(user);
    }

    public List<User> findUserFriends(int id) {
        return userStorage.getUser(id).getFriendIds().stream()
                .mapToInt(Long::intValue)
                .mapToObj(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public List<User> findUserMutualFriends(int id, int otherId) {
        List<User> userFriends = findUserFriends(id);
        List<User> otherUserFriends = findUserFriends(otherId);

        return userFriends.stream().filter(otherUserFriends::contains).collect(Collectors.toList());
    }
}