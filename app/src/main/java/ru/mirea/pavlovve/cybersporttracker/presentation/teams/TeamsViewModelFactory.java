package ru.mirea.pavlovve.cybersporttracker.presentation.teams;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.pavlovve.cybersporttracker.presentation.AppContainer;

public class TeamsViewModelFactory implements ViewModelProvider.Factory {

    private final AppContainer container;

    public TeamsViewModelFactory(AppContainer container) {
        this.container = container;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TeamsViewModel.class)) {
            return (T) new TeamsViewModel(
                    container.teams(),
                    container.addToFavorites(),
                    container.removeFromFavorites(),
                    container.profile(),
                    container.logout(),
                    container.observeFavoriteTeams()
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
