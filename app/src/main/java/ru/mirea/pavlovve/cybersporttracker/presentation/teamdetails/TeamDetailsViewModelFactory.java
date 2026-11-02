package ru.mirea.pavlovve.cybersporttracker.presentation.teamdetails;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.pavlovve.cybersporttracker.presentation.AppContainer;

public class TeamDetailsViewModelFactory implements ViewModelProvider.Factory {

    private final AppContainer container;
    private final int teamId;

    public TeamDetailsViewModelFactory(AppContainer container, int teamId) {
        this.container = container;
        this.teamId = teamId;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TeamDetailsViewModel.class)) {
            return (T) new TeamDetailsViewModel(
                    teamId,
                    container.teamById(),
                    container.matches(),
                    container.addToFavorites(),
                    container.removeFromFavorites(),
                    container.profile(),
                    container.observeFavoriteTeams()
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
