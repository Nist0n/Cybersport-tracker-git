package ru.mirea.pavlovve.cybersporttracker.data.dto;

import com.google.gson.annotations.SerializedName;

/** Краткая ссылка на лигу внутри команды или матча. */
public class LolLeagueRefDto {

    @SerializedName("name")
    public String name;

    @SerializedName("region")
    public String region;
}
