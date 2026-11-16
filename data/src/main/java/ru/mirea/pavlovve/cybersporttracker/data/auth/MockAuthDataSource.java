package ru.mirea.pavlovve.cybersporttracker.data.auth;

import ru.mirea.pavlovve.cybersporttracker.data.dto.UserDto;

public class MockAuthDataSource implements AuthDataSource {

    private final MockUserStore userStore;

    public MockAuthDataSource(MockUserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public boolean isConfigured() {
        return true;
    }

    @Override
    public UserDto signIn(String login, String password) {
        return userStore.authenticate(login, password);
    }

    @Override
    public UserDto signUp(String login, String password) {
        return userStore.createUser(login, password);
    }

    @Override
    public void signOut() {

    }
}
