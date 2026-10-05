package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import java.util.Collections;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Tournament;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.TournamentRepository;

public class GetTournamentsUseCase {
    private final TournamentRepository tournamentRepository;

    public GetTournamentsUseCase(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public List<Tournament> execute() {
        List<Tournament> tournaments = tournamentRepository.getTournaments();
        return tournaments == null ? Collections.<Tournament>emptyList() : tournaments;
    }

    public Tournament execute(int tournamentId) {
        if (tournamentId <= 0) {
            return null;
        }
        return tournamentRepository.getTournamentById(tournamentId);
    }
}
