package ru.mirea.pavlovve.cybersporttracker.data.auth;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.dto.UserDto;

/**
 * Локальное хранилище тестовых пользователей.
 *
 * <p>Авторизация не относится к контрольному заданию по Retrofit, поэтому
 * демонстрационные аккаунты остаются в памяти. Сетевой слой больше не
 * содержит захардкоженных JSON-заглушек.</p>
 */
public class MockUserStore {

    private final List<UserDto> users = new ArrayList<>();
    private int sequence = 100;

    public MockUserStore() {
        users.add(create(1, "admin@test.ru", "123456", "CyberTracker"));
        users.add(create(2, "viewer@test.ru", "viewer", "viewer"));
        sequence = users.size() + 1;
    }

    public UserDto authenticate(String login, String password) {
        for (UserDto user : users) {
            if (user.login.equals(login) && user.password.equals(password)) {
                return user;
            }
        }
        return null;
    }

    public UserDto createUser(String login, String password) {
        for (UserDto existing : users) {
            if (existing.login.equals(login)) {
                return null;
            }
        }
        UserDto user = create(sequence++, login, password, login);
        user.email = login + "@cybersport-tracker.ru";
        users.add(user);
        return user;
    }

    private UserDto create(int id, String login, String password, String nickname) {
        UserDto user = new UserDto();
        user.id = id;
        user.login = login;
        user.password = password;
        user.nickname = nickname;
        user.email = login;
        user.avatar_url = "https://i.pravatar.cc/150?u=" + login;
        return user;
    }
}
