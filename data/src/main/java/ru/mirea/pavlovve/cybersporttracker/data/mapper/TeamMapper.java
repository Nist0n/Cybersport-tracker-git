package ru.mirea.pavlovve.cybersporttracker.data.mapper;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.dto.TeamDto;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;

public final class TeamMapper {

    private TeamMapper() {
    }

    public static Team toDomain(TeamDto dto) {
        if (dto == null) {
            return null;
        }
        return new Team(
                dto.id,
                dto.tag,
                dto.name,
                dto.country,
                dto.logo_url,
                dto.world_rank,
                dto.players == null ? null : new ArrayList<>(dto.players)
        );
    }

    public static List<Team> toDomainList(List<TeamDto> dtos) {
        List<Team> result = new ArrayList<>();
        if (dtos == null) {
            return result;
        }
        for (TeamDto dto : dtos) {
            Team team = toDomain(dto);
            if (team != null) {
                result.add(team);
            }
        }
        return result;
    }
}
