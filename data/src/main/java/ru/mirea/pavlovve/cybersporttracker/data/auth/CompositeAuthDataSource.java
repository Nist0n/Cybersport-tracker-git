package ru.mirea.pavlovve.cybersporttracker.data.auth;

import ru.mirea.pavlovve.cybersporttracker.data.dto.UserDto;

public class CompositeAuthDataSource implements AuthDataSource {

    private final AuthDataSource firebase;
    private final AuthDataSource fallback;

    public CompositeAuthDataSource(AuthDataSource firebase, AuthDataSource fallback) {
        this.firebase = firebase;
        this.fallback = fallback;
    }

    private AuthDataSource active() {
        return firebase.isConfigured() ? firebase : fallback;
    }

    @Override
    public boolean isConfigured() {
        return active().isConfigured();
    }

    @Override
    public UserDto signIn(String login, String password) {
        if (firebase.isConfigured()) {
            UserDto result = firebase.signIn(login, password);
            if (result != null) {
                return result;
            }
        }
        return fallback.signIn(login, password);
    }

    @Override
    public UserDto signUp(String login, String password) {
        if (firebase.isConfigured()) {
            UserDto result = firebase.signUp(login, password);
            if (result != null) {
                return result;
            }
        }
        return fallback.signUp(login, password);
    }

    @Override
    public void signOut() {
        firebase.signOut();
        fallback.signOut();
    }
}
