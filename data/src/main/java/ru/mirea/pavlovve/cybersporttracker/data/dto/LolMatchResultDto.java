package ru.mirea.pavlovve.cybersporttracker.data.dto;

import com.google.gson.annotations.SerializedName;

/** Результат команды в матче. */
public class LolMatchResultDto {

    @SerializedName("outcome")
    public String outcome;

    @SerializedName("gameWins")
    public Integer gameWins;
}
