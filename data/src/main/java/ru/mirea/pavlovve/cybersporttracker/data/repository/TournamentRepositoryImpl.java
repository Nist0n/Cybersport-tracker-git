package ru.mirea.pavlovve.cybersporttracker.data.repository;

import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.mapper.TournamentMapper;
import ru.mirea.pavlovve.cybersporttracker.data.network.NetworkApi;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Tournament;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.TournamentRepository;

public class TournamentRepositoryImpl implements TournamentRepository {

    private final NetworkApi networkApi;

    public TournamentRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
    }

    @Override
    public List<Tournament> getTournaments() {
        return TournamentMapper.toDomainList(networkApi.getTournaments());
    }

    @Override
    public Tournament getTournamentById(int id) {
        return TournamentMapper.toDomain(networkApi.getTournamentById(id));
    }
}
