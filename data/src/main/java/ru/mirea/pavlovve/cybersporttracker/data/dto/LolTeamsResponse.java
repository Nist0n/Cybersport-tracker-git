package ru.mirea.pavlovve.cybersporttracker.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** Корневой ответ {@code getTeams}: {"data":{"teams":[...]}}. */
public class LolTeamsResponse {

    @SerializedName("data")
    public Data data;

    public static class Data {
        @SerializedName("teams")
        public List<LolTeamDto> teams;
    }
}
