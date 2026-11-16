package ru.mirea.pavlovve.cybersporttracker.presentation.teams;

import java.util.Objects;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;

public final class TeamListItem {

    private final Team team;
    private final boolean favorite;
    private final boolean authorized;

    public TeamListItem(Team team, boolean favorite, boolean authorized) {
        this.team = team;
        this.favorite = favorite;
        this.authorized = authorized;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamListItem)) {
            return false;
        }
        TeamListItem that = (TeamListItem) o;
        return favorite == that.favorite
                && authorized == that.authorized
                && team.getId() == that.team.getId()
                && Objects.equals(team.getName(), that.team.getName())
                && Objects.equals(team.getLogoUrl(), that.team.getLogoUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(team.getId(), team.getName(), team.getLogoUrl(),
                favorite, authorized);
    }
}
