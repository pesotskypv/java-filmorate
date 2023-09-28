package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendDao;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendDao friendDao) {
        this.userStorage = userStorage;
        this.friendDao = friendDao;
    }

    private final UserStorage userStorage;
    private final FriendDao friendDao;

    public User createUser(User user) {
        validateUser(user);

        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        int id = user.getId();

        validateUser(user);
        userStorage.getUser(id); // Проверка существования пользователя
        userStorage.updateUser(user);

        return getUser(id);
    }

    public List<User> findAllUsers() {
        List<User> users = userStorage.findAllUsers();
        for (User user : users) {
            user.addFriendIds(friendDao.findUserFriendsIds(user.getId()));
        }
        return users;
    }

    public User getUser(int id) {
        User user = userStorage.getUser(id);
        user.addFriendIds(friendDao.findUserFriendsIds(user.getId()));
        return user;
    }

    public User addUserFriend(int id, int friendId) {
        userStorage.getUser(id); // Проверка существования пользователя
        userStorage.getUser(friendId); // Проверка существования друга
        friendDao.addUserFriendId(id, friendId);
        return getUser(id);
    }

    public User removeUserFriend(int id, int friendId) {
        userStorage.getUser(id); // Проверка существования пользователя

        friendDao.removeUserFriendId(id, friendId);

        return getUser(id);
    }

    public List<User> findUserFriends(int id) {
        List<User> friends = friendDao.findUserFriends(id);
        for (User user : friends) {
            user.addFriendIds(friendDao.findUserFriendsIds(user.getId()));
        }
        return friends;
    }

    public List<User> findUserMutualFriends(int id, int otherId) {
        List<User> mutualFriends = friendDao.findUserMutualFriends(id, otherId);
        for (User user : mutualFriends) {
            user.addFriendIds(friendDao.findUserFriendsIds(user.getId()));
        }
        return mutualFriends;
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}