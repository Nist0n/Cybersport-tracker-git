package ru.mirea.pavlovve.cybersporttracker.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** Корневой ответ {@code getLeagues}: {"data":{"leagues":[...]}}. */
public class LolLeaguesResponse {

    @SerializedName("data")
    public Data data;

    public static class Data {
        @SerializedName("leagues")
        public List<LolLeagueDto> leagues;
    }
}
