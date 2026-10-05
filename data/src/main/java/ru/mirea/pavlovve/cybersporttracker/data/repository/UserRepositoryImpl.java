package ru.mirea.pavlovve.cybersporttracker.data.repository;

import java.time.LocalDateTime;

import ru.mirea.pavlovve.cybersporttracker.data.auth.AuthDataSource;
import ru.mirea.pavlovve.cybersporttracker.data.dto.UserDto;
import ru.mirea.pavlovve.cybersporttracker.data.mapper.ClientInfoMapper;
import ru.mirea.pavlovve.cybersporttracker.data.mapper.UserMapper;
import ru.mirea.pavlovve.cybersporttracker.data.storage.ClientStorage;
import ru.mirea.pavlovve.cybersporttracker.domain.models.User;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.UserRepository;

public class UserRepositoryImpl implements UserRepository {

    private final AuthDataSource authDataSource;
    private final ClientStorage clientStorage;

    public UserRepositoryImpl(AuthDataSource authDataSource, ClientStorage clientStorage) {
        this.authDataSource = authDataSource;
        this.clientStorage = clientStorage;
    }

    @Override
    public User login(String login, String password) {
        return saveSession(authDataSource.signIn(login, password));
    }

    @Override
    public User register(String login, String password) {
        return saveSession(authDataSource.signUp(login, password));
    }

    @Override
    public User getProfile() {
        return ClientInfoMapper.toUser(clientStorage.get());
    }

    @Override
    public void logout() {
        authDataSource.signOut();
        clientStorage.clear();
    }

    private User saveSession(UserDto dto) {
        User user = UserMapper.toDomain(dto);
        if (user == null) {
            return null;
        }
        clientStorage.save(ClientInfoMapper.toClientInfo(user,
                LocalDateTime.now().toString()));
        return user;
    }
}
