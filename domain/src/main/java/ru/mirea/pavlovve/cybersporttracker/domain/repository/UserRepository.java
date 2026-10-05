package ru.mirea.pavlovve.cybersporttracker.domain.repository;

import ru.mirea.pavlovve.cybersporttracker.domain.models.User;

public interface UserRepository {

    User login(String login, String password);

    User register(String login, String password);

    User getProfile();

    void logout();
}
