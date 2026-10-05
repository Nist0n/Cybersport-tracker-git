package ru.mirea.pavlovve.cybersporttracker.data.mapper;

import ru.mirea.pavlovve.cybersporttracker.data.dto.MatchDto;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Match;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;

public final class MatchMapper {

    private MatchMapper() {
    }

    public static Match toDomain(MatchDto dto, Team homeTeam, Team awayTeam) {
        if (dto == null || homeTeam == null || awayTeam == null) {
            return null;
        }
        return new Match(
                dto.id,
                dto.tournament_name,
                homeTeam,
                awayTeam,
                dto.home_score,
                dto.away_score,
                dto.start_time,
                dto.status
        );
    }
}
