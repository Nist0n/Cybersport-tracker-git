package ru.mirea.pavlovve.cybersporttracker.data.dto;

import com.google.gson.annotations.SerializedName;

/** Лига (турнир) в ответе {@code getLeagues}. */
public class LolLeagueDto {

    @SerializedName("id")
    public String id;

    @SerializedName("slug")
    public String slug;

    @SerializedName("name")
    public String name;

    @SerializedName("region")
    public String region;

    @SerializedName("image")
    public String image;
}
