package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Tournament;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.TournamentSubscriptionRepository;

public class SubscribeToTournamentUseCase {
    private final TournamentSubscriptionRepository subscriptionRepository;

    public SubscribeToTournamentUseCase(TournamentSubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public boolean execute(Tournament tournament) {
        if (tournament == null || tournament.getId() <= 0) {
            return false;
        }
        return subscriptionRepository.subscribe(tournament);
    }
}
