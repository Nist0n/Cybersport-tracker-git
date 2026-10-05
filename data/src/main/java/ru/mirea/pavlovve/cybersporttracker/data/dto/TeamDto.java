package ru.mirea.pavlovve.cybersporttracker.data.dto;

import java.util.ArrayList;
import java.util.List;

public class TeamDto {
    public int id;
    public String tag;
    public String name;
    public String country;
    public String logo_url;
    public int world_rank;
    public List<String> players = new ArrayList<>();
}
