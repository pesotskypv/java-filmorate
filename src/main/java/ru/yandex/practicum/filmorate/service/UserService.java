package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendDao;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

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

        userStorage.getUser(id); // Проверка существования пользователя
        validateUser(user);
        userStorage.updateUser(user);

        return getUser(id);
    }

    public List<User> findAllUsers() {
        return friendingUsers(userStorage.findAllUsers());
    }

    public User getUser(int id) {
        return friendingUsers(List.of(userStorage.getUser(id))).get(0);
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
        return friendingUsers(friendDao.findUserFriends(id));
    }

    public List<User> findUserMutualFriends(int id, int otherId) {
        return friendingUsers(friendDao.findUserMutualFriends(id, otherId));
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private List<User> friendingUsers(List<User> users) {
        Map<Integer, List<Long>> userFriends = new HashMap<>();

        if (users.isEmpty()) {
            return users;
        }

        List<Integer> userIds = users.stream().map(User::getId).collect(Collectors.toList());

        for (Friend friend : friendDao.findFriends(userIds)) {
            List<Long> friendsIds;
            int userId = friend.getUserId();

            if (userFriends.containsKey(userId)) {
                friendsIds = userFriends.get(userId);
            } else {
                friendsIds = new ArrayList<>();
            }
            friendsIds.add((long) friend.getFriendId());
            userFriends.put(userId, friendsIds);
        }
        for (User user : users) {
            int userId = user.getId();

            if (userFriends.containsKey(userId)) {
                user.addFriendIds(userFriends.get(userId));
            }
        }

        return users;
    }
}