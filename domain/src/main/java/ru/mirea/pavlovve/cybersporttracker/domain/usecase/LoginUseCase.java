package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import ru.mirea.pavlovve.cybersporttracker.domain.models.User;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.UserRepository;

public class LoginUseCase {
    private final UserRepository userRepository;

    public LoginUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String login, String password) {
        if (login == null || login.trim().isEmpty()) {
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            return null;
        }
        return userRepository.login(login.trim(), password);
    }
}
