package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import ru.mirea.pavlovve.cybersporttracker.domain.repository.UserRepository;

public class LogoutUseCase {

    private final UserRepository userRepository;

    public LogoutUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute() {
        userRepository.logout();
    }
}
