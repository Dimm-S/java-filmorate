package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Array;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    private Set<Genre> genres;
    private Set<Integer> likedUsersIds = new HashSet<>();

    public Film(int id, String name, String description, Date releaseDate, int duration, Mpa mpa, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate.toLocalDate();
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }

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
