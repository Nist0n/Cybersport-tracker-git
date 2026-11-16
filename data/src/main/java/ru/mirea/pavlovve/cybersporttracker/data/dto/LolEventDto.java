package ru.mirea.pavlovve.cybersporttracker.data.dto;

import com.google.gson.annotations.SerializedName;

/** Один матч из расписания {@code getSchedule}. */
public class LolEventDto {

    @SerializedName("startTime")
    public String startTime;

    /** completed / inProgress / unstarted. */
    @SerializedName("state")
    public String state;

    @SerializedName("blockName")
    public String blockName;

    @SerializedName("league")
    public LolLeagueRefDto league;

    @SerializedName("match")
    public LolMatchDto match;
}
