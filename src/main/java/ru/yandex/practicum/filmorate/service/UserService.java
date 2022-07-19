package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.Set;

@Service
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
}
