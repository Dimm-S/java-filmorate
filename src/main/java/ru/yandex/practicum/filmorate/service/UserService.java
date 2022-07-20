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
        inMemoryUserStorage.addUser(user);
        return user;
    }

    public User updateUser(User user) {
        inMemoryUserStorage.updateUser(user);
        return user;
    }

    public User getUser(Integer id) {
        return inMemoryUserStorage.getUser(id);
    }

    public boolean checkUsrById (Integer id) {
        return inMemoryUserStorage.checkUserById(id);
    }

    public Set<User> getAllFriendsByUserId (Integer id) {
        return inMemoryUserStorage.getAllFriendsByUserId(id);
    }

    public Set<User> getMutualFriends(Integer id, Integer otherId) {
        return inMemoryUserStorage.getMutualFriends(id, otherId);
    }

    public void addFriend(Integer id, Integer friendId) {
        inMemoryUserStorage.addFriend(id, friendId);
    }

    public void removeFriend(Integer id, Integer friendId) {
        inMemoryUserStorage.removeFriend(id, friendId);
    }

    public void addLike(Integer id, Integer userId) {
       inMemoryUserStorage.addLike(id, userId);
    }

    public void removeLike(Integer id, Integer userId) {
        inMemoryUserStorage.removeLike(id, userId);
    }

    public void validate(User user) {
        if (!user.getEmail().contains("@")) {
            log.debug("Неверный формат емейла {}", user.getEmail());
            throw new ValidationException("Неверный формат емейла");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Неверная дата {}", user.getBirthday());
            throw new ValidationException("Неверная дата");
        }
    }

    public void checkId(Integer id) {
        if (!inMemoryUserStorage.checkUserById(id)) {
            log.debug("Неверный id {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не существует пользователя с таким id");
        }
    }
}
