package ru.mirea.pavlovve.cybersporttracker.presentation.common;

import androidx.annotation.DrawableRes;

import ru.mirea.pavlovve.cybersporttracker.R;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;

public final class TeamLogoResolver {

    private TeamLogoResolver() {
    }

    @DrawableRes
    public static int resolve(Team team) {
        if (team == null) {
            return R.drawable.logo_team_placeholder;
        }
        switch (team.getId()) {
            case 1:
                return R.drawable.logo_navi;
            case 2:
                return R.drawable.logo_spirit;
            case 3:
                return R.drawable.logo_faze;
            case 4:
                return R.drawable.logo_g2;
            case 5:
                return R.drawable.logo_vitality;
            default:
                return R.drawable.logo_team_placeholder;
        }
    }
}
