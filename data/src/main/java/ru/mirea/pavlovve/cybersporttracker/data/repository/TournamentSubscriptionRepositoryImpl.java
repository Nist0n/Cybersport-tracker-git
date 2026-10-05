package ru.mirea.pavlovve.cybersporttracker.data.repository;

import java.util.Collections;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.local.dao.SubscriptionDao;
import ru.mirea.pavlovve.cybersporttracker.data.mapper.SubscriptionEntityMapper;
import ru.mirea.pavlovve.cybersporttracker.data.storage.ClientStorage;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Tournament;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.TournamentSubscriptionRepository;

public class TournamentSubscriptionRepositoryImpl
        implements TournamentSubscriptionRepository {

    private final SubscriptionDao subscriptionDao;
    private final ClientStorage clientStorage;

    public TournamentSubscriptionRepositoryImpl(SubscriptionDao subscriptionDao,
                                                ClientStorage clientStorage) {
        this.subscriptionDao = subscriptionDao;
        this.clientStorage = clientStorage;
    }

    @Override
    public boolean subscribe(Tournament tournament) {
        if (tournament == null || clientStorage.get() == null) {
            return false;
        }

        for (Tournament existing : getSubscribedTournaments()) {
            if (existing.getId() == tournament.getId()) {
                return false;
            }
        }
        subscriptionDao.insert(SubscriptionEntityMapper.toEntity(tournament));
        return true;
    }

    @Override
    public boolean unsubscribe(int tournamentId) {
        if (clientStorage.get() == null) {
            return false;
        }
        subscriptionDao.deleteById(tournamentId);
        return true;
    }

    @Override
    public List<Tournament> getSubscribedTournaments() {
        if (clientStorage.get() == null) {
            return Collections.emptyList();
        }
        return SubscriptionEntityMapper.toDomainList(subscriptionDao.getAll());
    }
}
