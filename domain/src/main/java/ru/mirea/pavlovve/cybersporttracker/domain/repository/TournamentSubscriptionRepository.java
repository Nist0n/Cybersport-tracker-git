package ru.mirea.pavlovve.cybersporttracker.domain.repository;

import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Tournament;

public interface TournamentSubscriptionRepository {
    boolean subscribe(Tournament tournament);
    boolean unsubscribe(int tournamentId);
    List<Tournament> getSubscribedTournaments();
}
