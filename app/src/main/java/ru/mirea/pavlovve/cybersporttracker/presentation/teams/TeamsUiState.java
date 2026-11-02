package ru.mirea.pavlovve.cybersporttracker.presentation.teams;

import java.util.Collections;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.domain.models.User;

public final class TeamsUiState {

    private final boolean loading;
    private final User user;
    private final List<TeamListItem> teams;
    private final int totalCount;
    private final int foundCount;
    private final String query;
    private final String country;
    private final boolean empty;

    public TeamsUiState(boolean loading, User user, List<TeamListItem> teams,
                        int totalCount, int foundCount, String query,
                        String country, boolean empty) {
        this.loading = loading;
        this.user = user;
        this.teams = Collections.unmodifiableList(teams);
        this.totalCount = totalCount;
        this.foundCount = foundCount;
        this.query = query;
        this.country = country;
        this.empty = empty;
    }

    public boolean isLoading() {
        return loading;
    }

    public User getUser() {
        return user;
    }

    public boolean isAuthorized() {
        return user != null;
    }

    public List<TeamListItem> getTeams() {
        return teams;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getFoundCount() {
        return foundCount;
    }

    public String getQuery() {
        return query;
    }

    public String getCountry() {
        return country;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean hasFilter() {
        return (query != null && !query.isEmpty())
                || (country != null && !country.isEmpty());
    }
}
