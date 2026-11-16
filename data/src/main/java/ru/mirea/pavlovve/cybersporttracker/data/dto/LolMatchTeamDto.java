package ru.mirea.pavlovve.cybersporttracker.data.dto;

import com.google.gson.annotations.SerializedName;

/** Команда внутри матча: имя, код, логотип и счёт. */
public class LolMatchTeamDto {

    @SerializedName("name")
    public String name;

    @SerializedName("code")
    public String code;

    @SerializedName("image")
    public String image;

    @SerializedName("result")
    public LolMatchResultDto result;
}
