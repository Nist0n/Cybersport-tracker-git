package ru.mirea.pavlovve.cybersporttracker.presentation.login;

import ru.mirea.pavlovve.cybersporttracker.domain.models.User;

public final class LoginUiState {

    public enum Error {
        NONE,
        EMPTY_FIELDS,
        LOGIN_FAILED,
        REGISTER_FAILED
    }

    private final boolean loading;
    private final Error error;
    private final User user;

    public LoginUiState(boolean loading, Error error, User user) {
        this.loading = loading;
        this.error = error;
        this.user = user;
    }

    public boolean isLoading() {
        return loading;
    }

    public Error getError() {
        return error;
    }

    public User getUser() {
        return user;
    }
}
