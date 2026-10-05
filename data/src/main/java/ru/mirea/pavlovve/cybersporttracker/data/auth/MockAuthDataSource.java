package ru.mirea.pavlovve.cybersporttracker.data.auth;

import ru.mirea.pavlovve.cybersporttracker.data.dto.UserDto;
import ru.mirea.pavlovve.cybersporttracker.data.network.NetworkApi;

public class MockAuthDataSource implements AuthDataSource {

    private final NetworkApi networkApi;

    public MockAuthDataSource(NetworkApi networkApi) {
        this.networkApi = networkApi;
    }

    @Override
    public boolean isConfigured() {
        return true;
    }

    @Override
    public UserDto signIn(String login, String password) {
        return networkApi.authenticate(login, password);
    }

    @Override
    public UserDto signUp(String login, String password) {
        return networkApi.createUser(login, password);
    }

    @Override
    public void signOut() {

    }
}
