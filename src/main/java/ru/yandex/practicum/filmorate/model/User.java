package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    public User(int id, String email, String login, String name, Date birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday.toLocalDate();
    }

    public void addFriend(Integer id) {
        friends.add(id);
    }

    public void removeFriend(Integer id) {
        friends.remove(id);
    }
}
