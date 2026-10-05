package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.TeamRepository;

public class GetTeamByIdUseCase {
    private final TeamRepository teamRepository;

    public GetTeamByIdUseCase(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }
    public Team execute(int teamId) {
        if (teamId <= 0) {
            return null;
        }
        return teamRepository.getTeamById(teamId);
    }
}
