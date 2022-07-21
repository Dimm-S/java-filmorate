package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Integer> likedUsersIds = new HashSet<>();

    public void addLike(Integer id) {
        this.likedUsersIds.add(id);
    }

    public void removeLike(Integer id) {
        this.likedUsersIds.remove(id);
    }

    public int getLikes() {
        return likedUsersIds.size();
    }
}
