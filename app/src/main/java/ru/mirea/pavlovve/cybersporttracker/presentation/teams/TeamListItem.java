package ru.mirea.pavlovve.cybersporttracker.presentation.teams;

import androidx.annotation.DrawableRes;

import java.util.Objects;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;

public final class TeamListItem {

    private final Team team;
    @DrawableRes
    private final int logoResId;
    private final boolean favorite;
    private final boolean authorized;

    public TeamListItem(Team team, @DrawableRes int logoResId,
                        boolean favorite, boolean authorized) {
        this.team = team;
        this.logoResId = logoResId;
        this.favorite = favorite;
        this.authorized = authorized;
    }

    public Team getTeam() {
        return team;
    }

    @DrawableRes
    public int getLogoResId() {
        return logoResId;
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
        return logoResId == that.logoResId
                && favorite == that.favorite
                && authorized == that.authorized
                && team.getId() == that.team.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(team.getId(), logoResId, favorite, authorized);
    }
}
