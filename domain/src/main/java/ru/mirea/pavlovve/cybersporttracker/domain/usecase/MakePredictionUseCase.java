package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Prediction;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.PredictionRepository;

public class MakePredictionUseCase {
    private static final String SCORE_PATTERN = "\\d{1,2}:\\d{1,2}";

    private final PredictionRepository predictionRepository;

    public MakePredictionUseCase(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    public boolean execute(Prediction prediction) {
        if (prediction == null || prediction.getMatchId() <= 0) {
            return false;
        }
        if (prediction.getPredictedTeamTag() == null
                || prediction.getPredictedTeamTag().trim().isEmpty()) {
            return false;
        }
        if (prediction.getPredictedScore() == null
                || !prediction.getPredictedScore().matches(SCORE_PATTERN)) {
            return false;
        }
        if (prediction.getCoefficient() < 1.0) {
            return false;
        }
        return predictionRepository.makePrediction(prediction);
    }
}
