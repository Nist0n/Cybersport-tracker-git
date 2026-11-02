package ru.mirea.pavlovve.cybersporttracker.presentation.teamdetails;

import java.util.Collections;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Match;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;

public final class TeamDetailsUiState {

    private final boolean loading;
    private final Team team;
    private final List<Match> matches;
    private final boolean favorite;
    private final boolean authorized;

    public TeamDetailsUiState(boolean loading, Team team, List<Match> matches,
                              boolean favorite, boolean authorized) {
        this.loading = loading;
        this.team = team;
        this.matches = Collections.unmodifiableList(matches);
        this.favorite = favorite;
        this.authorized = authorized;
    }

    public boolean isLoading() {
        return loading;
    }

    public Team getTeam() {
        return team;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public boolean isNotFound() {
        return !loading && team == null;
    }
}
