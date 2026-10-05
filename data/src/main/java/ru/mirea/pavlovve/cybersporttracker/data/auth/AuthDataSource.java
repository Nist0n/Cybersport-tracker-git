package ru.mirea.pavlovve.cybersporttracker.data.auth;

import ru.mirea.pavlovve.cybersporttracker.data.dto.UserDto;

public interface AuthDataSource {

    boolean isConfigured();

    UserDto signIn(String login, String password);

    UserDto signUp(String login, String password);

    void signOut();
}
