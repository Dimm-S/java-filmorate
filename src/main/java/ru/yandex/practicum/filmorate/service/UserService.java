package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<User> getUsers() {
        return inMemoryUserStorage.getUsers();
    }

    public User addUser(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        validate(user);
        inMemoryUserStorage.addUser(user);
        return user;
    }

    public User updateUser(User user) {
        checkId(user.getId());
        validate(user);
        inMemoryUserStorage.updateUser(user);
        return user;
    }

    public User getUser(Integer id) {
        checkId(id);
        return inMemoryUserStorage.getUser(id);
    }

    public Set<User> getAllFriendsByUserId (Integer id) {
        checkId(id);
        return inMemoryUserStorage.getAllFriendsByUserId(id);
    }

    public Set<User> getMutualFriends(Integer id, Integer otherId) {
        checkId(id);
        checkId(otherId);
        return inMemoryUserStorage.getMutualFriends(id, otherId);
    }

    public void addFriend(Integer id, Integer friendId) {
        checkId(id);
        checkId(friendId);
        inMemoryUserStorage.addFriend(id, friendId);
    }

    public void removeFriend(Integer id, Integer friendId) {
        checkId(id);
        checkId(friendId);
        inMemoryUserStorage.removeFriend(id, friendId);
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
        if (!inMemoryUserStorage.checkUserById(id)) {
            log.debug("Неверный id {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не существует пользователя с таким id");
        }
    }
}
