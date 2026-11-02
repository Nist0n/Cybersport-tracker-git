package ru.mirea.pavlovve.cybersporttracker.presentation.teams;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.pavlovve.cybersporttracker.R;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;
import ru.mirea.pavlovve.cybersporttracker.domain.models.User;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.AddTeamToFavoritesUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.GetTeamsUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.GetUserProfileUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.LogoutUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.RemoveTeamFromFavoritesUseCase;
import ru.mirea.pavlovve.cybersporttracker.presentation.common.SingleLiveEvent;
import ru.mirea.pavlovve.cybersporttracker.presentation.common.TeamLogoResolver;

public class TeamsViewModel extends ViewModel {

    private final GetTeamsUseCase getTeamsUseCase;
    private final AddTeamToFavoritesUseCase addTeamToFavoritesUseCase;
    private final RemoveTeamFromFavoritesUseCase removeTeamFromFavoritesUseCase;
    private final LogoutUseCase logoutUseCase;
    private final User user;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<Team>> networkTeams =
            new MutableLiveData<>(Collections.emptyList());

    private final LiveData<List<Team>> favoriteTeams;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    private final MutableLiveData<String> query = new MutableLiveData<>("");
    private final MutableLiveData<String> country = new MutableLiveData<>("");
    private final MediatorLiveData<TeamsUiState> state = new MediatorLiveData<>();
    private final SingleLiveEvent<Integer> message = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> loggedOut = new SingleLiveEvent<>();

    private volatile Set<Integer> favoriteIds = Collections.emptySet();

    public TeamsViewModel(GetTeamsUseCase getTeamsUseCase,
                          AddTeamToFavoritesUseCase addTeamToFavoritesUseCase,
                          RemoveTeamFromFavoritesUseCase removeTeamFromFavoritesUseCase,
                          GetUserProfileUseCase getUserProfileUseCase,
                          LogoutUseCase logoutUseCase,
                          LiveData<List<Team>> favoriteTeams) {
        this.getTeamsUseCase = getTeamsUseCase;
        this.addTeamToFavoritesUseCase = addTeamToFavoritesUseCase;
        this.removeTeamFromFavoritesUseCase = removeTeamFromFavoritesUseCase;
        this.logoutUseCase = logoutUseCase;
        this.user = getUserProfileUseCase.execute();
        this.favoriteTeams = favoriteTeams;

        state.addSource(networkTeams, teams -> emitState());
        state.addSource(loading, isLoading -> emitState());
        state.addSource(query, value -> emitState());
        state.addSource(country, value -> emitState());
        state.addSource(favoriteTeams, teams -> {
            Set<Integer> ids = new HashSet<>();
            if (teams != null) {
                for (Team team : teams) {
                    ids.add(team.getId());
                }
            }
            favoriteIds = ids;
            emitState();
        });

        loadTeams();
    }

    public LiveData<TeamsUiState> getState() {
        return state;
    }

    public LiveData<Integer> getMessage() {
        return message;
    }

    public LiveData<Boolean> getLoggedOut() {
        return loggedOut;
    }

    public void setQuery(String value) {
        String normalized = value == null ? "" : value.trim();
        if (!normalized.equals(query.getValue())) {
            query.setValue(normalized);
        }
    }

    public void setCountry(String value) {
        String normalized = value == null ? "" : value.trim();
        if (!normalized.equals(country.getValue())) {
            country.setValue(normalized);
        }
    }

    public void toggleFavorite(Team team) {
        if (team == null) {
            return;
        }
        if (user == null) {
            message.setValue(R.string.teams_login_hint);
            return;
        }
        executor.execute(() -> {
            if (favoriteIds.contains(team.getId())) {
                removeTeamFromFavoritesUseCase.execute(team.getId());
            } else {
                addTeamToFavoritesUseCase.execute(team);
            }
        });
    }

    public void logout() {
        executor.execute(() -> {
            logoutUseCase.execute();
            loggedOut.postValue(true);
        });
    }

    private void loadTeams() {
        executor.execute(() -> {
            List<Team> loaded = getTeamsUseCase.execute();
            networkTeams.postValue(loaded == null
                    ? Collections.emptyList()
                    : new ArrayList<>(loaded));
            loading.postValue(false);
        });
    }

    private void emitState() {
        List<Team> all = networkTeams.getValue();
        if (all == null) {
            all = Collections.emptyList();
        }
        boolean isLoading = Boolean.TRUE.equals(loading.getValue());
        String currentQuery = query.getValue() == null ? "" : query.getValue();
        String currentCountry = country.getValue() == null ? "" : country.getValue();

        List<TeamListItem> visible = new ArrayList<>();
        for (Team team : all) {
            if (!matchesCountry(team, currentCountry)) {
                continue;
            }
            if (!matchesQuery(team, currentQuery)) {
                continue;
            }
            visible.add(new TeamListItem(
                    team,
                    TeamLogoResolver.resolve(team),
                    user != null && favoriteIds.contains(team.getId()),
                    user != null
            ));
        }

        state.setValue(new TeamsUiState(
                isLoading,
                user,
                visible,
                all.size(),
                visible.size(),
                currentQuery,
                currentCountry,
                !isLoading && visible.isEmpty()
        ));
    }

    private boolean matchesQuery(Team team, String value) {
        if (value.isEmpty()) {
            return true;
        }
        String needle = value.toLowerCase(Locale.ROOT);
        return team.getTag().toLowerCase(Locale.ROOT).contains(needle)
                || team.getName().toLowerCase(Locale.ROOT).contains(needle)
                || team.getCountry().toLowerCase(Locale.ROOT).contains(needle);
    }

    private boolean matchesCountry(Team team, String value) {
        return value.isEmpty() || value.equalsIgnoreCase(team.getCountry());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdownNow();
    }
}
