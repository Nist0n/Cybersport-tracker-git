package ru.mirea.pavlovve.cybersporttracker.data.mapper;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.local.entity.PredictionEntity;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Prediction;

public final class PredictionEntityMapper {

    private PredictionEntityMapper() {
    }

    public static PredictionEntity toEntity(Prediction prediction) {
        if (prediction == null) {
            return null;
        }
        return new PredictionEntity(
                0,
                prediction.getMatchId(),
                prediction.getPredictedTeamTag(),
                prediction.getPredictedScore(),
                prediction.getCoefficient(),
                prediction.getStatus()
        );
    }

    public static Prediction toDomain(PredictionEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Prediction(
                entity.getId(),
                entity.getMatchId(),
                entity.getTeamTag(),
                entity.getScore(),
                entity.getCoefficient(),
                entity.getStatus()
        );
    }

    public static List<Prediction> toDomainList(List<PredictionEntity> entities) {
        List<Prediction> result = new ArrayList<>();
        if (entities == null) {
            return result;
        }
        for (PredictionEntity entity : entities) {
            Prediction prediction = toDomain(entity);
            if (prediction != null) {
                result.add(prediction);
            }
        }
        return result;
    }
}
