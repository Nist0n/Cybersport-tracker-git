package ru.mirea.pavlovve.cybersporttracker.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** Команда в ответе эндпоинта {@code getTeams}. */
public class LolTeamDto {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("code")
    public String code;

    @SerializedName("image")
    public String image;

    @SerializedName("alternativeImage")
    public String alternativeImage;

    @SerializedName("backgroundImage")
    public String backgroundImage;

    @SerializedName("status")
    public String status;

    @SerializedName("homeLeague")
    public LolLeagueRefDto homeLeague;

    @SerializedName("players")
    public List<LolPlayerDto> players;
}
