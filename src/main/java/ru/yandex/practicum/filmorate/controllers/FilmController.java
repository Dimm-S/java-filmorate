package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final LocalDate RELEASE_DATE_LIMIT = LocalDate.of(1895, 12, 28);
    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получен запрос на добавление фильма");
        validate(film);
        filmService.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос на изменение фильма");
        checkId(film.getId());
        validate(film);
        filmService.updateFilm(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id);
        userService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        checkUserId(userId);
        filmService.removeLike(id);
        userService.removeLike(id, userId);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        log.info("Запрос фильма");
        checkId(id);
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public Set<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    private void validate(Film film) {
        if (film.getName().isBlank()) {
            log.debug("Отсутствует наименование фильма");
            throw new ValidationException("Отсутствует наименование фильма");
        }
        if (film.getDescription().length() > 200) {
            log.debug("Слишком длинное описание");
            throw new ValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE_LIMIT)) {
            log.debug("Неверная дата {}", film.getReleaseDate());
            throw new ValidationException("Неверная дата релиза");
        }
        if (film.getDuration() <= 0) {
            log.debug("Неверная продолжительность {}", film.getDuration());
            throw new ValidationException("Неверная продолжительность фильма");
        }
    }

    private void checkId(Integer id) {
        if (!filmService.checkFilmById(id)) {
            log.debug("Неверный id {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не существует фильма с таким id");
        }

    }

    private void checkUserId(Integer id) {
        if (!userService.checkUsrById(id)) {
            log.debug("Неверный id пользователя {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
