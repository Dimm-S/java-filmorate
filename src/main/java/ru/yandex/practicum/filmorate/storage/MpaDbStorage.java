package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpa(Integer id) {
        final String sqlQuery = "select * from mpaa where mpaa_id = ?";
        if ((jdbcTemplate.query(sqlQuery, this::makeMpa, id)).size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::makeMpa, id);
    }

    public Collection<Mpa> getAllMpa() {
        final String sqlQuery = "select * from mpaa";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("mpaa_id"), rs.getString("mpaa_name"));
    }
}
