package ru.mirea.pavlovve.cybersporttracker.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** Корневой ответ {@code getSchedule}: {"data":{"schedule":{"events":[...]}}}. */
public class LolScheduleResponse {

    @SerializedName("data")
    public Data data;

    public static class Data {
        @SerializedName("schedule")
        public Schedule schedule;
    }

    public static class Schedule {
        @SerializedName("events")
        public List<LolEventDto> events;
    }
}
