package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void addFilmWithEmptyNameTest() {
        final FilmController controller = new FilmController();
        final Film film = new Film();
        film.setDescription("Sex, drugs and rock'n'roll");
        film.setReleaseDate(LocalDate.of(2005, 9, 15));
        film.setDuration(360);
        assertThrows(RuntimeException.class, () -> controller.addFilm(film));
    }

    @Test
    void addFilmWithTooLongDescriptionNameTest() {
        final FilmController controller = new FilmController();
        final Film film = new Film();
        film.setName("name");
        film.setDescription("sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss" +
                "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss" +
                "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss" +
                "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss" +
                "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        film.setReleaseDate(LocalDate.of(2005, 9, 15));
        film.setDuration(360);
        assertThrows(RuntimeException.class, () -> controller.addFilm(film));
    }

    @Test
    void addFilmWithWrongDateTest() {
        final FilmController controller = new FilmController();
        final Film film = new Film();
        film.setName("name");
        film.setDescription("Sex, drugs and rock'n'roll");
        film.setReleaseDate(LocalDate.of(1685, 9, 15));
        film.setDuration(360);
        assertThrows(RuntimeException.class, () -> controller.addFilm(film));
    }

    @Test
    void addFilmWithWrongDurationTest() {
        final FilmController controller = new FilmController();
        final Film film = new Film();
        film.setName("name");
        film.setDescription("Sex, drugs and rock'n'roll");
        film.setReleaseDate(LocalDate.of(2005, 9, 15));
        film.setDuration(-360);
        assertThrows(RuntimeException.class, () -> controller.addFilm(film));
    }

    @Test
    void updateFilmWithWrongIdTest() {
        final FilmController controller = new FilmController();
        final Film film = new Film();
        film.setId(2);
        film.setName("name");
        film.setDescription("Sex, drugs and rock'n'roll");
        film.setReleaseDate(LocalDate.of(2005, 9, 15));
        film.setDuration(360);
        assertThrows(RuntimeException.class, () -> controller.updateFilm(film));
    }

    @Test
    void updateFilmTest() {
        final FilmController controller = new FilmController();
        final Film film = new Film();
        film.setName("Very good film");
        film.setDescription("Sex, drugs and rock'n'roll");
        film.setReleaseDate(LocalDate.of(2005, 9, 15));
        film.setDuration(360);
        controller.addFilm(film);
        final Film filmUpdater = new Film();
        filmUpdater.setId(1);
        filmUpdater.setName("Very good film");
        filmUpdater.setDescription("Black Jack and....");
        filmUpdater.setReleaseDate(LocalDate.of(2005, 9, 15));
        filmUpdater.setDuration(360);
        controller.updateFilm(filmUpdater);
        assertEquals("Black Jack and....", controller.getFilm(1).getDescription());
    }
}