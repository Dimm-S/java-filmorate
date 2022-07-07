package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int newId = 0;

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Получен запрос на добавление пользователя");
        if (!user.getEmail().contains("@")) {
            log.debug("Неверный формат емейла {}", user.getEmail());
            throw new ValidationException("Неверный формат емейла");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Неверная дата {}", user.getBirthday());
            throw new ValidationException("Неверная дата");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Получен запрос на изменение пользователя");
        if (!users.containsKey(user.getId())) {
            log.debug("Неверный id {}", user.getId());
            throw new ValidationException("Не существует пользователя с таким id");
        }
        if (!users.get(user.getId()).getLogin().equals(user.getLogin())) {
            log.debug("Неверный логин {}", user.getLogin());
            throw new ValidationException("Неверный логин");
        }
        users.replace(user.getId(), user);
        return user;
    }

    public User getUser(Integer id){
        return users.get(id);
    }
    private int generateId() {
        return ++newId;
    }
}
