package ru.mirea.pavlovve.cybersporttracker.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** Матч: две команды и их результаты. */
public class LolMatchDto {

    @SerializedName("id")
    public String id;

    @SerializedName("teams")
    public List<LolMatchTeamDto> teams;
}
