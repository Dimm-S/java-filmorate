package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Получен запрос на добавление пользователя");
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        validate(user);
        userService.addUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Получен запрос на изменение пользователя");
        checkId(user.getId());
        validate(user);
        userService.updateUser(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        checkId(id);
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getAllFriendsByUserId(@PathVariable Integer id) {
        log.info("Получен запрос на отображения списка друзей");
        checkId(id);
        return userService.getAllFriendsByUserId(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        checkId(id);
        checkId(otherId);
        return userService.getMutualFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        checkId(id);
        checkId(friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        checkId(id);
        checkId(friendId);
        userService.removeFriend(id, friendId);
    }


    private void validate(User user) {
        if (!user.getEmail().contains("@")) {
            log.debug("Неверный формат емейла {}", user.getEmail());
            throw new ValidationException("Неверный формат емейла");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Неверная дата {}", user.getBirthday());
            throw new ValidationException("Неверная дата");
        }
    }

    private void checkId(Integer id) {
        if (!userService.checkUsrById(id)) {
            log.debug("Неверный id {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не существует пользователя с таким id");
        }
    }
}
