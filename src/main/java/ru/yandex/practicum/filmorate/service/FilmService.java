package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class FilmService {
    private static final LocalDate RELEASE_DATE_LIMIT = LocalDate.of(1895, 12, 28);
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

    public void validate(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Отсутствует наименование фильма");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE_LIMIT)) {
            throw new ValidationException("Неверная дата релиза");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Неверная продолжительность фильма");
        }
    }

    public void checkId(Integer id) {
        if (!inMemoryFilmStorage.checkFilmById(id)) {
            log.debug("Неверный id фильма {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не существует фильма с таким id");
        }

    }

    public void checkUserId(Integer id) {
        if (!inMemoryFilmStorage.checkFilmById(id)) {
            log.debug("Неверный id пользователя {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
