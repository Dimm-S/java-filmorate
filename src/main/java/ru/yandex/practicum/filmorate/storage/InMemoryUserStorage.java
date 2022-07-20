package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int newId = 0;

    public Collection<User> getUsers() {
        return users.values();
    }

    public User addUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }
    
    public User updateUser(User user) {
        users.replace(user.getId(), user);
        return user;
    }

    public User getUser(Integer id) {
        return users.get(id);
    }

    public boolean checkUserById (Integer id) {
        return users.containsKey(id);
    }

    public Set<User> getAllFriendsByUserId (Integer id) {
        Set<User> friends = new HashSet<>();
        for (Integer ids : users.get(id).getFriends()) {
            friends.add(users.get(ids));
        }
        return friends;
    }

    public Set<User> getMutualFriends(Integer id, Integer otherId) {
        Set<Integer> userFriendsIds = users.get(id).getFriends();
        Set<Integer> otherUserFriendsIds = users.get(otherId).getFriends();
        Set<Integer> mutualFriendsIds = new HashSet<>();
        for (Integer friendId : userFriendsIds) {
            for (Integer otherFriendId : otherUserFriendsIds) {
                if (friendId == otherFriendId) {
                    mutualFriendsIds.add(friendId);
                }
            }
        }
        Set<User> mutualFriends = new HashSet<>();
        for (Integer mfId : mutualFriendsIds) {
            mutualFriends.add(users.get(mfId));
        }
        return mutualFriends;
    }

    public void addFriend(Integer id, Integer friendId) {
        users.get(id).addFriend(friendId);
        users.get(friendId).addFriend(id);
    }

    public void removeFriend(Integer id, Integer friendId) {
        users.get(id).removeFriend(friendId);
        users.get(friendId).removeFriend(id);
    }

    private int generateId() {
        return ++newId;
    }
}
