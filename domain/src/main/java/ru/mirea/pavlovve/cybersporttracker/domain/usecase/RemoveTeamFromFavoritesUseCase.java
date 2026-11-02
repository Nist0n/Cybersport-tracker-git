package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import ru.mirea.pavlovve.cybersporttracker.domain.repository.FavoritesRepository;

public class RemoveTeamFromFavoritesUseCase {

    private final FavoritesRepository favoritesRepository;

    public RemoveTeamFromFavoritesUseCase(FavoritesRepository favoritesRepository) {
        this.favoritesRepository = favoritesRepository;
    }

    public boolean execute(int teamId) {
        if (teamId <= 0) {
            return false;
        }
        return favoritesRepository.removeTeamFromFavorites(teamId);
    }
}
