package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {
    Collection<Film> getFilms();
    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film getFilm(Integer id);
    Set<Film> getPopularFilms(int count);
    void addLike(Integer id, Integer userId);
    void removeLike(Integer id, Integer userId);
    boolean checkFilmById(Integer id);
}
