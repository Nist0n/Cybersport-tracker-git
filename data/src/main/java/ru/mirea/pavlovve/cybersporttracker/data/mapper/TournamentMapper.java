package ru.mirea.pavlovve.cybersporttracker.data.mapper;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.dto.LolLeagueDto;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Tournament;

/** Преобразование лиги из API в доменную модель турнира. */
public final class TournamentMapper {

    private TournamentMapper() {
    }

    public static Tournament toDomain(LolLeagueDto dto) {
        if (dto == null || dto.name == null) {
            return null;
        }
        return new Tournament(
                TeamMapper.toId(dto.id),
                dto.name,
                "League of Legends",
                dto.region == null ? "" : dto.region,
                "",
                "—",
                "ACTIVE"
        );
    }

    public static List<Tournament> toDomainList(List<LolLeagueDto> dtos) {
        List<Tournament> result = new ArrayList<>();
        if (dtos == null) {
            return result;
        }
        for (LolLeagueDto dto : dtos) {
            Tournament tournament = toDomain(dto);
            if (tournament != null) {
                result.add(tournament);
            }
        }
        return result;
    }
}
