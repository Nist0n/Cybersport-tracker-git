package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import ru.mirea.pavlovve.cybersporttracker.domain.models.User;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.UserRepository;

public class RegisterUseCase {
    private final UserRepository userRepository;

    public RegisterUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String login, String password) {
        if (login == null || login.trim().isEmpty()) {
            return null;
        }
        if (password == null || password.length() < 4) {
            return null;
        }
        return userRepository.register(login.trim(), password);
    }
}
