package ru.mirea.pavlovve.cybersporttracker.domain.models;

public class Tournament {
    private final int id;
    private final String name;
    private final String game;
    private final String location;
    private final String startDate;
    private final String prizePool;
    private final String status;

    public Tournament(int id, String name, String game, String location,
                      String startDate, String prizePool, String status) {
        this.id = id;
        this.name = name;
        this.game = game;
        this.location = location;
        this.startDate = startDate;
        this.prizePool = prizePool;
        this.status = status;
    }

    public int getId() {
        return id;
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

    public String getShortInfo() {
        return name + " [" + game + "] — призовые " + prizePool + ", старт " + startDate;
    }

    @Override
    public String toString() {
        return getShortInfo();
    }
}
