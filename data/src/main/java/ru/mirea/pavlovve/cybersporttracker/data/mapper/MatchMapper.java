package ru.mirea.pavlovve.cybersporttracker.data.mapper;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import ru.mirea.pavlovve.cybersporttracker.data.dto.LolEventDto;
import ru.mirea.pavlovve.cybersporttracker.data.dto.LolMatchTeamDto;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Match;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;

/** Преобразование события расписания в доменную модель матча. */
public final class MatchMapper {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.getDefault());

    private MatchMapper() {
    }

    public static Match toDomain(LolEventDto event, Map<String, Team> teamsByKey) {
        if (event == null || event.match == null || event.match.teams == null
                || event.match.teams.size() < 2) {
            return null;
        }
        LolMatchTeamDto homeDto = event.match.teams.get(0);
        LolMatchTeamDto awayDto = event.match.teams.get(1);
        Team homeTeam = resolve(homeDto, teamsByKey);
        Team awayTeam = resolve(awayDto, teamsByKey);
        if (homeTeam == null || awayTeam == null) {
            return null;
        }
        return new Match(
                TeamMapper.toId(event.match.id),
                tournamentName(event),
                homeTeam,
                awayTeam,
                gameWins(homeDto),
                gameWins(awayDto),
                formatStartTime(event.startTime),
                status(event.state)
        );
    }

    /** Ищет команду в общем списке по коду или названию. */
    public static Team resolve(LolMatchTeamDto dto, Map<String, Team> teamsByKey) {
        if (dto == null) {
            return null;
        }
        Team team = teamsByKey.get(key(dto.code));
        if (team == null) {
            team = teamsByKey.get(key(dto.name));
        }
        return team != null ? team : TeamMapper.fromMatchTeam(dto);
    }

    public static String key(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private static String tournamentName(LolEventDto event) {
        String league = event.league == null || event.league.name == null
                ? ""
                : event.league.name;
        String block = event.blockName == null || event.blockName.isEmpty()
                ? ""
                : " · " + event.blockName;
        return league + block;
    }

    private static int gameWins(LolMatchTeamDto dto) {
        if (dto == null || dto.result == null || dto.result.gameWins == null) {
            return 0;
        }
        return dto.result.gameWins;
    }

    private static String status(String state) {
        if ("completed".equalsIgnoreCase(state)) {
            return Match.STATUS_FINISHED;
        }
        if ("inProgress".equalsIgnoreCase(state)) {
            return Match.STATUS_LIVE;
        }
        return Match.STATUS_SCHEDULED;
    }

    private static String formatStartTime(String isoTime) {
        if (isoTime == null || isoTime.isEmpty()) {
            return "";
        }
        try {
            return OffsetDateTime.parse(isoTime)
                    .atZoneSameInstant(ZoneId.systemDefault())
                    .format(TIME_FORMAT);
        } catch (RuntimeException e) {
            return isoTime;
        }
    }
}
