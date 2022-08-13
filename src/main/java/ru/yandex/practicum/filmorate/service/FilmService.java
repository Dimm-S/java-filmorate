package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class FilmService {
    private static final LocalDate RELEASE_DATE_LIMIT = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmsInDatabase") FilmStorage filmStorage,
                       @Qualifier("usersInDatabase") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }


    public Film addFilm(Film film) {
        validate(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        checkFilmById(film.getId());
        validate(film);
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(Integer id) {
        checkFilmById(id);
        return filmStorage.getFilm(id);
    }

    public Set<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public void addLike(Integer id, Integer userId) {
        filmStorage.addLike(id, userId);
    }

    public void removeLike(Integer id, Integer userId) {
        checkFilmById(id);
        checkUserId(userId);
        filmStorage.removeLike(id, userId);
    }

    private void validate(Film film) {
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
        if (film.getMpa() == null) {
            throw new ValidationException("Отсутствует возрастной рейтинг");
        }
    }

    private void checkFilmById(Integer id) {
        if (!filmStorage.checkFilmById(id)) {
            log.debug("Неверный id фильма {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не существует фильма с таким id");
        }
    }

    private void checkUserId(Integer id) {
        if (!userStorage.checkUserById(id)) {
            log.debug("Неверный id пользователя {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
