package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.sql.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testGetUserById() {
        userStorage.addUser(new User(0, "user@mail", "user", "user", Date.valueOf("1975-12-18")));
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetFilmById() {
        filmStorage.addFilm(new Film(0, "name", "description", Date.valueOf("1992-05-15"), 120,
                new Mpa(1, "G"), new HashSet<Genre>()));
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", 1));
    }

}
