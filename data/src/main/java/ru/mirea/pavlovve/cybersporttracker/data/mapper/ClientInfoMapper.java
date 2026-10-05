package ru.mirea.pavlovve.cybersporttracker.data.mapper;

import ru.mirea.pavlovve.cybersporttracker.data.storage.model.ClientInfo;
import ru.mirea.pavlovve.cybersporttracker.domain.models.User;

public final class ClientInfoMapper {

    private ClientInfoMapper() {
    }

    public static ClientInfo toClientInfo(User user, String savedAt) {
        if (user == null) {
            return null;
        }
        return new ClientInfo(
                user.getId(),
                user.getLogin(),
                user.getNickname(),
                user.getEmail(),
                user.getAvatarUrl(),
                savedAt
        );
    }

    public static User toUser(ClientInfo info) {
        if (info == null) {
            return null;
        }
        return new User(
                info.getId(),
                info.getLogin(),
                info.getNickname(),
                info.getEmail(),
                info.getAvatarUrl()
        );
    }
}
