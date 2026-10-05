package ru.mirea.pavlovve.cybersporttracker.domain.models;

public class Match {
    public static final String STATUS_SCHEDULED = "SCHEDULED";
    public static final String STATUS_LIVE = "LIVE";
    public static final String STATUS_FINISHED = "FINISHED";

    private final int id;
    private final String tournamentName;
    private final Team homeTeam;
    private final Team awayTeam;
    private final int homeScore;
    private final int awayScore;
    private final String startTime;
    private final String status;

    public Match(int id, String tournamentName, Team homeTeam, Team awayTeam,
                 int homeScore, int awayScore, String startTime, String status) {
        this.id = id;
        this.tournamentName = tournamentName;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.startTime = startTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStatus() {
        return status;
    }

    public String getShortInfo() {
        String score = STATUS_SCHEDULED.equals(status)
                ? startTime
                : homeScore + ":" + awayScore + " (" + startTime + ")";
        return homeTeam.getTag() + " vs " + awayTeam.getTag() + " — " + score
                + " [" + status + ", " + tournamentName + "]";
    }

    @Override
    public String toString() {
        return getShortInfo();
    }
}
