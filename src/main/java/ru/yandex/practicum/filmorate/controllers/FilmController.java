package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final LocalDate RELEASE_DATE_LIMIT = LocalDate.of(1895, 12, 28);
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int newId = 0;

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получен запрос на добавление фильма");
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
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос на изменение фильма");
        if (!films.containsKey(film.getId())) {
            log.debug("Неверный id {}", film.getId());
            throw new ValidationException("Не существует фильма с таким id");
        }
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
        films.replace(film.getId(), film);
        return film;
    }

    public Film getFilm(Integer id) {
        return films.get(id);
    }
    private int generateId() {
        return ++newId;
    }
}
