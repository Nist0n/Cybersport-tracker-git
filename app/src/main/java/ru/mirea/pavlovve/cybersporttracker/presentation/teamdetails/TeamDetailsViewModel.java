package ru.mirea.pavlovve.cybersporttracker.presentation.teamdetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.pavlovve.cybersporttracker.R;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Match;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;
import ru.mirea.pavlovve.cybersporttracker.domain.models.User;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.AddTeamToFavoritesUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.GetMatchesUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.GetTeamByIdUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.GetUserProfileUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.RemoveTeamFromFavoritesUseCase;
import ru.mirea.pavlovve.cybersporttracker.presentation.common.SingleLiveEvent;

public class TeamDetailsViewModel extends ViewModel {

    private final int teamId;
    private final GetTeamByIdUseCase getTeamByIdUseCase;
    private final GetMatchesUseCase getMatchesUseCase;
    private final AddTeamToFavoritesUseCase addTeamToFavoritesUseCase;
    private final RemoveTeamFromFavoritesUseCase removeTeamFromFavoritesUseCase;
    private final User user;
    private final LiveData<List<Team>> favoriteTeams;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<Team> team = new MutableLiveData<>();
    private final MutableLiveData<List<Match>> matches =
            new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    private final MediatorLiveData<TeamDetailsUiState> state = new MediatorLiveData<>();
    private final SingleLiveEvent<Integer> message = new SingleLiveEvent<>();

    private volatile Set<Integer> favoriteIds = Collections.emptySet();

    public TeamDetailsViewModel(int teamId,
                                GetTeamByIdUseCase getTeamByIdUseCase,
                                GetMatchesUseCase getMatchesUseCase,
                                AddTeamToFavoritesUseCase addTeamToFavoritesUseCase,
                                RemoveTeamFromFavoritesUseCase removeTeamFromFavoritesUseCase,
                                GetUserProfileUseCase getUserProfileUseCase,
                                LiveData<List<Team>> favoriteTeams) {
        this.teamId = teamId;
        this.getTeamByIdUseCase = getTeamByIdUseCase;
        this.getMatchesUseCase = getMatchesUseCase;
        this.addTeamToFavoritesUseCase = addTeamToFavoritesUseCase;
        this.removeTeamFromFavoritesUseCase = removeTeamFromFavoritesUseCase;
        this.user = getUserProfileUseCase.execute();
        this.favoriteTeams = favoriteTeams;

        state.addSource(team, value -> emitState());
        state.addSource(matches, value -> emitState());
        state.addSource(loading, value -> emitState());
        state.addSource(favoriteTeams, teams -> {
            Set<Integer> ids = new HashSet<>();
            if (teams != null) {
                for (Team favorite : teams) {
                    ids.add(favorite.getId());
                }
            }
            favoriteIds = ids;
            emitState();
        });

        load();
    }

    public LiveData<TeamDetailsUiState> getState() {
        return state;
    }

    public LiveData<Integer> getMessage() {
        return message;
    }

    public void toggleFavorite() {
        Team current = team.getValue();
        if (current == null) {
            return;
        }
        if (user == null) {
            message.setValue(R.string.teams_login_hint);
            return;
        }
        executor.execute(() -> {
            if (favoriteIds.contains(current.getId())) {
                removeTeamFromFavoritesUseCase.execute(current.getId());
            } else {
                addTeamToFavoritesUseCase.execute(current);
            }
        });
    }

    private void load() {
        executor.execute(() -> {
            Team loaded = getTeamByIdUseCase.execute(teamId);
            team.postValue(loaded);

            List<Match> teamMatches = new ArrayList<>();
            if (loaded != null) {
                List<Match> all = getMatchesUseCase.execute();
                if (all != null) {
                    for (Match match : all) {
                        if (match.getHomeTeam().getId() == loaded.getId()
                                || match.getAwayTeam().getId() == loaded.getId()) {
                            teamMatches.add(match);
                        }
                    }
                }
            }
            matches.postValue(teamMatches);
            loading.postValue(false);
        });
    }

    private void emitState() {
        Team current = team.getValue();
        boolean isLoading = Boolean.TRUE.equals(loading.getValue());
        List<Match> currentMatches = matches.getValue();
        if (currentMatches == null) {
            currentMatches = Collections.emptyList();
        }
        boolean favorite = current != null
                && user != null
                && favoriteIds.contains(current.getId());
        state.setValue(new TeamDetailsUiState(
                isLoading,
                current,
                currentMatches,
                favorite,
                user != null
        ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdownNow();
    }
}
