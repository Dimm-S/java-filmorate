package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public Collection<Film> getFilms() {
        return inMemoryFilmStorage.getFilms();
    }


    public Film addFilm(Film film) {
        inMemoryFilmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        inMemoryFilmStorage.updateFilm(film);
        return film;
    }

    public Film getFilm(Integer id) {
        return inMemoryFilmStorage.getFilm(id);
    }

    public Set<Film> getPopularFilms(int count) {
        return inMemoryFilmStorage.getPopularFilms(count);
    }

    public void addLike(Integer id) {
        inMemoryFilmStorage.addLike(id);
    }

    public void removeLike(Integer id) {
        inMemoryFilmStorage.removeLike(id);
    }



    public boolean checkFilmById(Integer id) {
        return inMemoryFilmStorage.checkFilmById(id);
    }
}
