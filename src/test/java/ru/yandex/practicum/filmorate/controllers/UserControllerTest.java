package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void addUserWithWrongEmailTest() {
        final UserController controller = new UserController();
        final User user = new User();
        user.setEmail("yandex.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(2020, 12, 20));
        assertThrows(RuntimeException.class, () -> controller.addUser(user));
    }

    @Test
    void addUserWithWrongDateTest() {
        final UserController controller = new UserController();
        final User user = new User();
        user.setEmail("mail@yandex.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(2024, 12, 20));
        assertThrows(RuntimeException.class, () -> controller.addUser(user));
    }

    @Test
    void addUserBlankNameTest() {
        final UserController controller = new UserController();
        final User user = new User();
        user.setEmail("mail@yandex.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2020, 12, 20));
        controller.addUser(user);
        assertEquals("login", controller.getUser(1).getName());
    }

    @Test
    void updateUserWrongIdTest() {
        final UserController controller = new UserController();
        final User user = new User();
        user.setId(2);
        user.setEmail("mail@yandex.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(2020, 12, 20));
        assertThrows(RuntimeException.class, () -> controller.updateUser(user));
    }

    @Test
    void updateUserTest() {
        final UserController controller = new UserController();
        final User user = new User();
        user.setEmail("mail@yandex.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(2020, 12, 20));
        controller.addUser(user);
        final User userUpdater = new User();
        userUpdater.setId(1);
        userUpdater.setEmail("mail@yandex.ru");
        userUpdater.setLogin("login");
        userUpdater.setName("secondName");
        userUpdater.setBirthday(LocalDate.of(2020, 12, 20));
        controller.updateUser(userUpdater);
        assertEquals("secondName", controller.getUser(1).getName());
    }
}