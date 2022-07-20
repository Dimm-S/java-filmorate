package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int newId = 0;

    public Collection<Film> getFilms() {
        return films.values();
    }

    public Film addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        films.replace(film.getId(), film);
        return film;
    }

    public Film getFilm(Integer id) {
        return films.get(id);
    }

    public Set<Film> getPopularFilms(int count) {
        List<Film> filmsList = new ArrayList<>(films.values());
        TreeSet<Film> popularFilms = new TreeSet<>(Comparator.comparing(Film::getId)
                .thenComparing(Film::getLikes).reversed());
        popularFilms.addAll(filmsList);
        while (popularFilms.size() > count) {
            popularFilms.pollLast();
        }
        return popularFilms;
    }

    public void addLike(Integer id) {
        films.get(id).addLike();
    }

    public void removeLike(Integer id) {
        films.get(id).removeLike();
    }

    public boolean checkFilmById(Integer id) {
        return films.containsKey(id);
    }

    private int generateId() {
        return ++newId;
    }

}
