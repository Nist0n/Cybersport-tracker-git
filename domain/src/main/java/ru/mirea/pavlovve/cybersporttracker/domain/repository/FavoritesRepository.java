package ru.mirea.pavlovve.cybersporttracker.domain.repository;

import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;

public interface FavoritesRepository {

    boolean addTeamToFavorites(Team team);

    List<Team> getFavoriteTeams();

    boolean removeTeamFromFavorites(int teamId);
}
