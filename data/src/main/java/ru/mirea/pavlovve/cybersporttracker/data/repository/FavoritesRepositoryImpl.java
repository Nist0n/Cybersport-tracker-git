package ru.mirea.pavlovve.cybersporttracker.data.repository;

import java.util.Collections;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.local.dao.FavoriteTeamDao;
import ru.mirea.pavlovve.cybersporttracker.data.mapper.FavoriteTeamEntityMapper;
import ru.mirea.pavlovve.cybersporttracker.data.storage.ClientStorage;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.FavoritesRepository;

public class FavoritesRepositoryImpl implements FavoritesRepository {

    private final FavoriteTeamDao favoriteTeamDao;
    private final ClientStorage clientStorage;

    public FavoritesRepositoryImpl(FavoriteTeamDao favoriteTeamDao,
                                   ClientStorage clientStorage) {
        this.favoriteTeamDao = favoriteTeamDao;
        this.clientStorage = clientStorage;
    }

    @Override
    public boolean addTeamToFavorites(Team team) {
        if (team == null || clientStorage.get() == null) {
            return false;
        }

        for (Team existing : getFavoriteTeams()) {
            if (existing.getId() == team.getId()) {
                return false;
            }
        }
        favoriteTeamDao.insert(FavoriteTeamEntityMapper.toEntity(team));
        return true;
    }

    @Override
    public List<Team> getFavoriteTeams() {
        if (clientStorage.get() == null) {
            return Collections.emptyList();
        }
        return FavoriteTeamEntityMapper.toDomainList(favoriteTeamDao.getAll());
    }

    @Override
    public boolean removeTeamFromFavorites(int teamId) {
        if (clientStorage.get() == null) {
            return false;
        }
        favoriteTeamDao.deleteById(teamId);
        return true;
    }
}
