package ru.mirea.pavlovve.cybersporttracker.data.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.mirea.pavlovve.cybersporttracker.data.dto.TeamDto;
import ru.mirea.pavlovve.cybersporttracker.data.mapper.TeamMapper;
import ru.mirea.pavlovve.cybersporttracker.data.network.NetworkApi;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.TeamRepository;

public class TeamRepositoryImpl implements TeamRepository {

    private final NetworkApi networkApi;

    public TeamRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
    }

    @Override
    public List<Team> getTeams() {
        return TeamMapper.toDomainList(networkApi.getTeams());
    }

    @Override
    public Team getTeamById(int id) {
        return TeamMapper.toDomain(networkApi.getTeamById(id));
    }

    @Override
    public List<Team> searchTeams(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getTeams();
        }
        String needle = query.trim().toLowerCase(Locale.ROOT);
        List<Team> result = new ArrayList<>();
        for (Team team : getTeams()) {
            if (team.getTag().toLowerCase(Locale.ROOT).contains(needle)
                    || team.getName().toLowerCase(Locale.ROOT).contains(needle)
                    || team.getCountry().toLowerCase(Locale.ROOT).contains(needle)) {
                result.add(team);
            }
        }
        return result;
    }
}
