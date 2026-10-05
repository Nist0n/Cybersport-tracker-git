package ru.mirea.pavlovve.cybersporttracker.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.dto.MatchDto;
import ru.mirea.pavlovve.cybersporttracker.data.mapper.MatchMapper;
import ru.mirea.pavlovve.cybersporttracker.data.network.NetworkApi;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Match;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.MatchRepository;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.TeamRepository;

public class MatchRepositoryImpl implements MatchRepository {

    private final NetworkApi networkApi;
    private final TeamRepository teamRepository;

    public MatchRepositoryImpl(NetworkApi networkApi, TeamRepository teamRepository) {
        this.networkApi = networkApi;
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Match> getMatches() {
        List<Match> result = new ArrayList<>();
        for (MatchDto dto : networkApi.getMatches()) {
            Match match = toDomain(dto);
            if (match != null) {
                result.add(match);
            }
        }
        return result;
    }

    @Override
    public Match getMatchById(int id) {
        return toDomain(networkApi.getMatchById(id));
    }

    private Match toDomain(MatchDto dto) {
        if (dto == null) {
            return null;
        }
        Team homeTeam = teamRepository.getTeamById(dto.home_team_id);
        Team awayTeam = teamRepository.getTeamById(dto.away_team_id);
        return MatchMapper.toDomain(dto, homeTeam, awayTeam);
    }
}
