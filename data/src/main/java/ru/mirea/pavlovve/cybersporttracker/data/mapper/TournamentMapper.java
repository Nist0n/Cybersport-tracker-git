package ru.mirea.pavlovve.cybersporttracker.data.mapper;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.dto.TournamentDto;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Tournament;

public final class TournamentMapper {

    private TournamentMapper() {
    }

    public static Tournament toDomain(TournamentDto dto) {
        if (dto == null) {
            return null;
        }
        return new Tournament(
                dto.id,
                dto.name,
                dto.game,
                dto.location,
                dto.start_date,
                dto.prize_pool,
                dto.status
        );
    }

    public static List<Tournament> toDomainList(List<TournamentDto> dtos) {
        List<Tournament> result = new ArrayList<>();
        if (dtos == null) {
            return result;
        }
        for (TournamentDto dto : dtos) {
            Tournament tournament = toDomain(dto);
            if (tournament != null) {
                result.add(tournament);
            }
        }
        return result;
    }
}
