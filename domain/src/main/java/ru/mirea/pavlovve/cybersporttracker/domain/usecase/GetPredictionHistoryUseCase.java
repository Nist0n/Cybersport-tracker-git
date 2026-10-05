package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import java.util.Collections;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Prediction;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.PredictionRepository;

public class GetPredictionHistoryUseCase {
    private final PredictionRepository predictionRepository;

    public GetPredictionHistoryUseCase(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    public List<Prediction> execute() {
        List<Prediction> history = predictionRepository.getPredictionHistory();
        return history == null ? Collections.<Prediction>emptyList() : history;
    }
}
