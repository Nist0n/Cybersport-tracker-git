package ru.mirea.pavlovve.cybersporttracker.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "predictions")
public class PredictionEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int matchId;
    private String teamTag;
    private String score;
    private double coefficient;
    private String status;

    public PredictionEntity(int id, int matchId, String teamTag,
                            String score, double coefficient, String status) {
        this.id = id;
        this.matchId = matchId;
        this.teamTag = teamTag;
        this.score = score;
        this.coefficient = coefficient;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getMatchId() {
        return matchId;
    }

    public String getTeamTag() {
        return teamTag;
    }

    public String getScore() {
        return score;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public String getStatus() {
        return status;
    }
}
