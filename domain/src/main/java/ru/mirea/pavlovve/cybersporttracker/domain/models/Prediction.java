package ru.mirea.pavlovve.cybersporttracker.domain.models;

public class Prediction {
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_WIN = "WIN";
    public static final String STATUS_LOSS = "LOSS";

    private final int id;
    private final int matchId;
    private final String predictedTeamTag;
    private final String predictedScore;
    private final double coefficient;
    private final String status;

    public Prediction(int id, int matchId, String predictedTeamTag,
                      String predictedScore, double coefficient, String status) {
        this.id = id;
        this.matchId = matchId;
        this.predictedTeamTag = predictedTeamTag;
        this.predictedScore = predictedScore;
        this.coefficient = coefficient;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getMatchId() {
        return matchId;
    }

    public String getPredictedTeamTag() {
        return predictedTeamTag;
    }

    public String getPredictedScore() {
        return predictedScore;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public String getStatus() {
        return status;
    }

    public String getShortInfo() {
        return "Матч #" + matchId + ": " + predictedTeamTag
                + " со счётом " + predictedScore
                + " (коэф. " + coefficient + ") [" + status + "]";
    }

    @Override
    public String toString() {
        return getShortInfo();
    }
}
