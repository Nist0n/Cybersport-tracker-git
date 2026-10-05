package ru.mirea.pavlovve.cybersporttracker.domain.repository;

import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;

public interface TeamRepository {
    List<Team> getTeams();

    Team getTeamById(int id);

    List<Team> searchTeams(String query);
}
