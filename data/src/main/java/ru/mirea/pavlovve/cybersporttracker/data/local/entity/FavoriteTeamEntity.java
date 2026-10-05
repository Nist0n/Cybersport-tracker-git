package ru.mirea.pavlovve.cybersporttracker.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_teams")
public class FavoriteTeamEntity {

    @PrimaryKey
    private int id;
    private String tag;
    private String name;
    private String country;
    private String logoUrl;
    private int worldRank;

    private String players;

    public FavoriteTeamEntity(int id, String tag, String name, String country,
                              String logoUrl, int worldRank, String players) {
        this.id = id;
        this.tag = tag;
        this.name = name;
        this.country = country;
        this.logoUrl = logoUrl;
        this.worldRank = worldRank;
        this.players = players;
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

    public String getPlayers() {
        return players;
    }
}
