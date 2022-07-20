package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    Collection<User> getUsers();
    User addUser(User user);
    User updateUser(User user);
    User getUser(Integer id);
    boolean checkUserById (Integer id);
    Set<User> getAllFriendsByUserId (Integer id);
    Set<User> getMutualFriends(Integer id, Integer otherId);
    void addFriend(Integer id, Integer friendId);
    void removeFriend(Integer id, Integer friendId);
}
