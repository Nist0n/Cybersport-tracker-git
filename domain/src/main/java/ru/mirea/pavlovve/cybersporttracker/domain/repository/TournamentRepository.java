package ru.mirea.pavlovve.cybersporttracker.domain.repository;

import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Tournament;

public interface TournamentRepository {
    List<Tournament> getTournaments();
    Tournament getTournamentById(int id);
}
