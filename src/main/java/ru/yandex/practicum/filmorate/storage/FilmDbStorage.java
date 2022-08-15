package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("filmsInDatabase")
public class FilmDbStorage implements FilmStorage{

    private JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getFilms() {
        final String sqlQuery = "select * from films f " +
                "left join mpaa m on f.mpaa_id = m.mpaa_id ";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilmFromDatabaseAnswer);
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into films(film_name, release_date, description, duration, mpaa_id) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId());
        String sqlQueryChk = "select * from films f " +
                "left join mpaa m on f.mpaa_id = m.mpaa_id " +
                "where film_name = ?";
        Film chkdFilm = jdbcTemplate.queryForObject(sqlQueryChk,
                this::makeFilmFromDatabaseAnswer, film.getName());
        film.setId(chkdFilm.getId());
        updateFilmGenres(film);
        return jdbcTemplate.queryForObject(sqlQueryChk, this::makeFilmFromDatabaseAnswer, film.getName());
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update films set " +
                "film_name = ?, release_date = ?, description = ?, duration = ?, MPAA_ID = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (film.getGenres() != null) {
            updateFilmGenres(film);
        }
        String sqlQueryChk = "select * from films f " +
                "left join mpaa m on f.mpaa_id = m.mpaa_id " +
                "where FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQueryChk, this::makeFilmFromDatabaseAnswer, film.getId());
    }

    @Override
    public Film getFilm(Integer id) {
        final String sqlQuery = "select * from films f " +
                "left join mpaa m on f.mpaa_id = m.mpaa_id " +
                "where f.FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::makeFilmFromDatabaseAnswer, id);

    }

    @Override
    public Set<Film> getPopularFilms(int count) {
        final String sqlQuery = "select * from films f " +
                "left join mpaa m on f.mpaa_id = m.mpaa_id " +
                "left join films_likes fl on f.film_id = fl.film_id " +
                "group by f.film_id " +
                "order by count(fl.user_id) desc limit ?";
        List<Film> popularFilms = jdbcTemplate.query(sqlQuery, this::makeFilmFromDatabaseAnswer, count);
        return new HashSet<>(popularFilms);
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        final String sqlQuery = "insert into films_likes(film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public void removeLike(Integer id, Integer userId) {
        String sqlQuery = "delete from films_likes where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public boolean checkFilmById(Integer id) {
        String sqlQuery = "select * from films f " +
                "left join mpaa m on f.mpaa_id = m.mpaa_id " +
                "where f.FILM_ID = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilmFromDatabaseAnswer, id);
        if (films.size() == 0) {
            return false;
        }
        return true;
    }

    private Film makeFilmFromDatabaseAnswer(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("film_name"),
                rs.getString("description"),
                rs.getDate("release_date"),
                rs.getInt("duration"),
                new Mpa(rs.getInt("mpaa_id"), rs.getString("mpaa_name")),
                getFilmGenres(rs.getInt("film_id")));
    }

    private Set<Genre> getFilmGenres(int id) {
        String sqlQuery = "select * from films f " +
                "left join FILMS_GENRES FG on f.FILM_ID = FG.FILM_ID " +
                "left join GENRES G on G.GENRE_ID = FG.GENRE_ID " +
                "where f.FILM_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, new GenreDbStorage(new JdbcTemplate())::makeGenre, id);
        if (genres.get(0).getId() == 0) {
            return new HashSet<>();
        }
        return new HashSet<>(genres);
    }

    private void updateFilmGenres(Film film) {
        Set<Genre> genres = film.getGenres();
        removeAllGenresByFilm(film);
        if (genres != null && genres.size() != 0 && genres.iterator().next().getId() != 0) {
            for (Genre genre : genres) {
                String sqlQuery = "insert into films_genres(FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlQuery,
                        film.getId(),
                        genre.getId());
            }
        }
    }

    private void removeAllGenresByFilm(Film film) {
        jdbcTemplate.update("delete from films_genres where FILM_ID = ?", film.getId());
    }
}
