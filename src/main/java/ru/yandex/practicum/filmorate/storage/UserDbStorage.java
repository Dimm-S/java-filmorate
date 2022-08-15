package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("usersInDatabase")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getUsers() {
        final String sqlQuery = "select * from USERS";
        List<User> users = jdbcTemplate.query(sqlQuery, this::makeUserFromDatabaseAnswer);
        return users;
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into users(user_name, login, email, birthday) " +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());
        String sqlQueryChk = "select * from users where EMAIL = ?";
        return jdbcTemplate.queryForObject(sqlQueryChk, this::makeUserFromDatabaseAnswer, user.getEmail());
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update users set " +
                "user_name = ?, login = ?, email = ?, birthday = ? where USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        String sqlQueryChk = "select * from users where USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQueryChk, this::makeUserFromDatabaseAnswer, user.getId());

    }

    @Override
    public User getUser(Integer id) {
        String sqlQuery = "select * from users where user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::makeUserFromDatabaseAnswer, id);
    }

    @Override
    public boolean checkUserById(Integer id) {
        String sqlQuery = "select * from users where user_id = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, this::makeUserFromDatabaseAnswer, id);
        if (users.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public Set<User> getAllFriendsByUserId(Integer id) {
        String sqlQuery = "select * from USERS\n" +
                "right join USER_FRIENDS UF on USERS.USER_ID = UF.FRIEND_ID where UF.USER_ID = ?";
        Set<User> friends = new HashSet<>(jdbcTemplate.query(sqlQuery, this::makeUserFromDatabaseAnswer, id));
        return friends;
    }

    @Override
    public Set<User> getMutualFriends(Integer id, Integer otherId) {
        String sqlQuery = "select * from users\n" +
                "right join user_friends on user_friends.friend_id = users.user_id where USER_FRIENDS.user_id = ?";
        List<User> friends1 = jdbcTemplate.query(sqlQuery, this::makeUserFromDatabaseAnswer, id);
        List<User> friends2 = jdbcTemplate.query(sqlQuery, this::makeUserFromDatabaseAnswer, otherId);
        Set<User> mutualFriends = new HashSet<>();
        for (User user : friends1) {
            for (User friend : friends2) {
                if(user.equals(friend)) {
                    mutualFriends.add(user);
                }
            }
        }
        return mutualFriends;
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        String sqlQuery = "insert into USER_FRIENDS (user_id, friend_id) VALUES ( ?, ? )";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void removeFriend(Integer id, Integer friendId) {
        String sqlQuery = "delete from user_friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    private User makeUserFromDatabaseAnswer(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getDate("birthday"));
    }
}
