package ru.mirea.pavlovve.cybersporttracker.data.mapper;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.dto.UserDto;
import ru.mirea.pavlovve.cybersporttracker.domain.models.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toDomain(UserDto dto) {
        if (dto == null) {
            return null;
        }
        return new User(
                dto.id,
                dto.login,
                dto.nickname,
                dto.email,
                dto.avatar_url
        );
    }
}
