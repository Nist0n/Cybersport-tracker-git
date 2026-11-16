package ru.mirea.pavlovve.cybersporttracker.data.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mirea.pavlovve.cybersporttracker.data.dto.LolEventDto;
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
        Map<String, Team> teamsByKey = new HashMap<>();
        for (Team team : teamRepository.getTeams()) {
            teamsByKey.put(MatchMapper.key(team.getName()), team);
            teamsByKey.put(MatchMapper.key(team.getTag()), team);
        }

        List<Match> result = new ArrayList<>();
        for (LolEventDto event : networkApi.getSchedule()) {
            Match match = MatchMapper.toDomain(event, teamsByKey);
            if (match != null) {
                result.add(match);
            }
        }
        return result;
    }

    @Override
    public Match getMatchById(int id) {
        for (Match match : getMatches()) {
            if (match.getId() == id) {
                return match;
            }
        }
        return null;
    }
}
