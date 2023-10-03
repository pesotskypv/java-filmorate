package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@Positive @PathVariable int id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addUserFriend(@Positive @PathVariable int id, @Positive @PathVariable int friendId) {
        return userService.addUserFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeUserFriend(@Positive @PathVariable int id, @Positive @PathVariable int friendId) {
        return userService.removeUserFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findUserFriends(@Positive @PathVariable int id) {
        return userService.findUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findUserMutualFriends(@Positive @PathVariable int id, @Positive @PathVariable int otherId) {
        return userService.findUserMutualFriends(id, otherId);
    }
}