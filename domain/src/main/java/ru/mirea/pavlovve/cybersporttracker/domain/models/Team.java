package ru.mirea.pavlovve.cybersporttracker.domain.models;

import java.util.Collections;
import java.util.List;

public class Team {
    private final int id;
    private final String tag;
    private final String name;
    private final String country;
    private final String logoUrl;
    private final int worldRank;
    private final List<String> players;

    public Team(int id, String tag, String name, String country,
                String logoUrl, int worldRank, List<String> players) {
        this.id = id;
        this.tag = tag;
        this.name = name;
        this.country = country;
        this.logoUrl = logoUrl;
        this.worldRank = worldRank;
        this.players = players == null
                ? Collections.<String>emptyList()
                : Collections.unmodifiableList(players);
    }

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public int getWorldRank() {
        return worldRank;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getShortInfo() {
        return tag + " — " + name + " (" + country + "), место в рейтинге: " + worldRank;
    }

    @Override
    public String toString() {
        return getShortInfo();
    }
}
