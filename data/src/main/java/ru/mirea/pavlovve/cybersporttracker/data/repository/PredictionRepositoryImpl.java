package ru.mirea.pavlovve.cybersporttracker.data.repository;

import java.util.Collections;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.local.dao.PredictionDao;
import ru.mirea.pavlovve.cybersporttracker.data.mapper.PredictionEntityMapper;
import ru.mirea.pavlovve.cybersporttracker.data.storage.ClientStorage;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Prediction;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.PredictionRepository;

public class PredictionRepositoryImpl implements PredictionRepository {

    private final PredictionDao predictionDao;
    private final ClientStorage clientStorage;

    public PredictionRepositoryImpl(PredictionDao predictionDao,
                                    ClientStorage clientStorage) {
        this.predictionDao = predictionDao;
        this.clientStorage = clientStorage;
    }

    @Override
    public boolean makePrediction(Prediction prediction) {
        if (prediction == null || clientStorage.get() == null) {
            return false;
        }
        predictionDao.insert(PredictionEntityMapper.toEntity(prediction));
        return true;
    }

    @Override
    public List<Prediction> getPredictionHistory() {
        if (clientStorage.get() == null) {
            return Collections.emptyList();
        }
        return PredictionEntityMapper.toDomainList(predictionDao.getAll());
    }
}
