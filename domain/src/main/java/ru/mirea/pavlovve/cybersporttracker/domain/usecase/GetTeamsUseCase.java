package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import java.util.Collections;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.TeamRepository;

public class GetTeamsUseCase {
    private final TeamRepository teamRepository;

    public GetTeamsUseCase(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> execute() {
        List<Team> teams = teamRepository.getTeams();
        return teams == null ? Collections.<Team>emptyList() : teams;
    }
    public List<Team> execute(String query) {
        if (query == null || query.trim().isEmpty()) {
            return execute();
        }
        List<Team> teams = teamRepository.searchTeams(query.trim());
        return teams == null ? Collections.<Team>emptyList() : teams;
    }
}
