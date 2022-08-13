package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenre(Integer id) {
        final String sqlQuery = "select * from genres where genre_id = ?";
        if ((jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, id)).size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return jdbcTemplate.queryForObject(sqlQuery, GenreDbStorage::makeGenre, id);
    }

    public Collection<Genre> getAllGenres() {
        final String sqlQuery = "select * from genres order by GENRE_ID asc ";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre);
    }

    public static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
    }
}
