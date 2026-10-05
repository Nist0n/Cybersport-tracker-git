package ru.mirea.pavlovve.cybersporttracker.data.mapper;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.local.entity.SubscriptionEntity;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Tournament;

public final class SubscriptionEntityMapper {

    private SubscriptionEntityMapper() {
    }

    public static SubscriptionEntity toEntity(Tournament tournament) {
        if (tournament == null) {
            return null;
        }
        return new SubscriptionEntity(
                tournament.getId(),
                tournament.getName(),
                tournament.getGame(),
                tournament.getLocation(),
                tournament.getStartDate(),
                tournament.getPrizePool(),
                tournament.getStatus()
        );
    }

    public static Tournament toDomain(SubscriptionEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Tournament(
                entity.getTournamentId(),
                entity.getName(),
                entity.getGame(),
                entity.getLocation(),
                entity.getStartDate(),
                entity.getPrizePool(),
                entity.getStatus()
        );
    }

    public static List<Tournament> toDomainList(List<SubscriptionEntity> entities) {
        List<Tournament> result = new ArrayList<>();
        if (entities == null) {
            return result;
        }
        for (SubscriptionEntity entity : entities) {
            Tournament tournament = toDomain(entity);
            if (tournament != null) {
                result.add(tournament);
            }
        }
        return result;
    }
}
