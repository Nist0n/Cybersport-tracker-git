package ru.mirea.pavlovve.cybersporttracker.presentation.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.pavlovve.cybersporttracker.domain.models.User;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.GetUserProfileUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.LoginUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.RegisterUseCase;

public class LoginViewModel extends ViewModel {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<LoginUiState> state = new MutableLiveData<>(
            new LoginUiState(false, LoginUiState.Error.NONE, null));

    public LoginViewModel(LoginUseCase loginUseCase,
                          RegisterUseCase registerUseCase,
                          GetUserProfileUseCase getUserProfileUseCase) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;

        User session = getUserProfileUseCase.execute();
        if (session != null) {
            state.setValue(new LoginUiState(false, LoginUiState.Error.NONE, session));
        }
    }

    public LiveData<LoginUiState> getState() {
        return state;
    }

    public void signIn(String email, String password) {
        if (isBlank(email) || isBlank(password)) {
            state.setValue(new LoginUiState(false, LoginUiState.Error.EMPTY_FIELDS, null));
            return;
        }
        state.setValue(new LoginUiState(true, LoginUiState.Error.NONE, null));
        executor.execute(() -> {
            User user = loginUseCase.execute(email.trim(), password);
            state.postValue(user == null
                    ? new LoginUiState(false, LoginUiState.Error.LOGIN_FAILED, null)
                    : new LoginUiState(false, LoginUiState.Error.NONE, user));
        });
    }

    public void signUp(String email, String password) {
        if (isBlank(email) || isBlank(password)) {
            state.setValue(new LoginUiState(false, LoginUiState.Error.EMPTY_FIELDS, null));
            return;
        }
        state.setValue(new LoginUiState(true, LoginUiState.Error.NONE, null));
        executor.execute(() -> {
            User user = registerUseCase.execute(email.trim(), password);
            state.postValue(user == null
                    ? new LoginUiState(false, LoginUiState.Error.REGISTER_FAILED, null)
                    : new LoginUiState(false, LoginUiState.Error.NONE, user));
        });
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdownNow();
    }
}
