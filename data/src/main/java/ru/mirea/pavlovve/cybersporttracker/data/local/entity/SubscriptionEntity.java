package ru.mirea.pavlovve.cybersporttracker.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subscriptions")
public class SubscriptionEntity {

    @PrimaryKey
    private int tournamentId;
    private String name;
    private String game;
    private String location;
    private String startDate;
    private String prizePool;
    private String status;

    public SubscriptionEntity(int tournamentId, String name, String game, String location,
                              String startDate, String prizePool, String status) {
        this.tournamentId = tournamentId;
        this.name = name;
        this.game = game;
        this.location = location;
        this.startDate = startDate;
        this.prizePool = prizePool;
        this.status = status;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public String getName() {
        return name;
    }

    public String getGame() {
        return game;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getPrizePool() {
        return prizePool;
    }

    public String getStatus() {
        return status;
    }
}
