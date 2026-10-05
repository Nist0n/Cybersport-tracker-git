package ru.mirea.pavlovve.cybersporttracker.domain.repository;

import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Prediction;

public interface PredictionRepository {
    boolean makePrediction(Prediction prediction);

    List<Prediction> getPredictionHistory();
}
