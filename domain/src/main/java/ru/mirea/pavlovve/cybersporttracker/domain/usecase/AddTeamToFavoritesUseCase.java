package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.FavoritesRepository;

public class AddTeamToFavoritesUseCase {
    private final FavoritesRepository favoritesRepository;

    public AddTeamToFavoritesUseCase(FavoritesRepository favoritesRepository) {
        this.favoritesRepository = favoritesRepository;
    }

    public boolean execute(Team team) {
        if (team == null || team.getId() <= 0) {
            return false;
        }
        return favoritesRepository.addTeamToFavorites(team);
    }
}
