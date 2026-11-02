package ru.mirea.pavlovve.cybersporttracker.presentation.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.pavlovve.cybersporttracker.presentation.AppContainer;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final AppContainer container;

    public LoginViewModelFactory(AppContainer container) {
        this.container = container;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(
                    container.login(),
                    container.register(),
                    container.profile()
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
