package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class FilmService {
    private static final LocalDate RELEASE_DATE_LIMIT = LocalDate.of(1895, 12, 28);
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<Film> getFilms() {
        return inMemoryFilmStorage.getFilms();
    }


    public Film addFilm(Film film) {
        validate(film);
        inMemoryFilmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        checkFilmById(film.getId());
        validate(film);
        inMemoryFilmStorage.updateFilm(film);
        return film;
    }

    public Film getFilm(Integer id) {
        checkFilmById(id);
        return inMemoryFilmStorage.getFilm(id);
    }

    public Set<Film> getPopularFilms(int count) {
        return inMemoryFilmStorage.getPopularFilms(count);
    }

    public void addLike(Integer id, Integer userId) {
        inMemoryFilmStorage.addLike(id, userId);
    }

    public void removeLike(Integer id, Integer userId) {
        checkFilmById(id);
        checkUserId(userId);
        inMemoryFilmStorage.removeLike(id, userId);
    }

    private void validate(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("?????????????????????? ???????????????????????? ????????????");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("?????????????? ?????????????? ????????????????");
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE_LIMIT)) {
            throw new ValidationException("???????????????? ???????? ????????????");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("???????????????? ?????????????????????????????????? ????????????");
        }
    }

    private void checkFilmById(Integer id) {
        if (!inMemoryFilmStorage.checkFilmById(id)) {
            log.debug("???????????????? id ???????????? {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "???? ???????????????????? ???????????? ?? ?????????? id");
        }
    }

    private void checkUserId(Integer id) {
        if (!inMemoryUserStorage.checkUserById(id)) {
            log.debug("???????????????? id ???????????????????????? {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
