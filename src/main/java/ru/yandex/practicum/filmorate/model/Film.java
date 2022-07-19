package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private int likes = 0;

    public void addLike() {
        this.likes++;
    }

    public void removeLike() {
        if (likes > 0) {
            this.likes--;
        }
    }
}
