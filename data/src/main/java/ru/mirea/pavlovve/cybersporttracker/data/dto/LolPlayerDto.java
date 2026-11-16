package ru.mirea.pavlovve.cybersporttracker.data.dto;

import com.google.gson.annotations.SerializedName;

/** Игрок в составе команды. */
public class LolPlayerDto {

    @SerializedName("summonerName")
    public String summonerName;

    @SerializedName("firstName")
    public String firstName;

    @SerializedName("lastName")
    public String lastName;

    @SerializedName("image")
    public String image;

    @SerializedName("role")
    public String role;
}
