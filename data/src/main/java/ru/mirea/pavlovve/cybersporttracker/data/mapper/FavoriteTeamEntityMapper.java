package ru.mirea.pavlovve.cybersporttracker.data.mapper;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.local.entity.FavoriteTeamEntity;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;

public final class FavoriteTeamEntityMapper {

    private static final String DELIMITER = ", ";

    private FavoriteTeamEntityMapper() {
    }

    public static FavoriteTeamEntity toEntity(Team team) {
        if (team == null) {
            return null;
        }
        return new FavoriteTeamEntity(
                team.getId(),
                team.getTag(),
                team.getName(),
                team.getCountry(),
                team.getLogoUrl(),
                team.getWorldRank(),
                String.join(DELIMITER, team.getPlayers())
        );
    }

    public static Team toDomain(FavoriteTeamEntity entity) {
        if (entity == null) {
            return null;
        }
        List<String> players = new ArrayList<>();
        if (entity.getPlayers() != null && !entity.getPlayers().isEmpty()) {
            for (String player : entity.getPlayers().split(DELIMITER)) {
                if (!player.isEmpty()) {
                    players.add(player);
                }
            }
        }
        return new Team(
                entity.getId(),
                entity.getTag(),
                entity.getName(),
                entity.getCountry(),
                entity.getLogoUrl(),
                entity.getWorldRank(),
                players
        );
    }

    public static List<Team> toDomainList(List<FavoriteTeamEntity> entities) {
        List<Team> result = new ArrayList<>();
        if (entities == null) {
            return result;
        }
        for (FavoriteTeamEntity entity : entities) {
            Team team = toDomain(entity);
            if (team != null) {
                result.add(team);
            }
        }
        return result;
    }
}
