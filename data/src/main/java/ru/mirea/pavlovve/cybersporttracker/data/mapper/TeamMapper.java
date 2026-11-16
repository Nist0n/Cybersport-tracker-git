package ru.mirea.pavlovve.cybersporttracker.data.mapper;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.dto.LolMatchTeamDto;
import ru.mirea.pavlovve.cybersporttracker.data.dto.LolPlayerDto;
import ru.mirea.pavlovve.cybersporttracker.data.dto.LolTeamDto;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;

/**
 * Преобразование сетевой модели команды в доменную.
 *
 * <p>Домен не должен зависеть от формата JSON, поэтому вся «грязная» работа
 * (строковые идентификаторы, http-ссылки, отсутствующие поля) выполняется здесь.</p>
 */
public final class TeamMapper {

    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final String DEAD_LOGO_HOST = "lolstatic-a.akamaihd.net";

    private TeamMapper() {
    }

    /**
     * Стабильный числовой идентификатор из строкового id API.
     * {@link String#hashCode()} детерминирован, поэтому один и тот же
     * идентификатор получается при каждом запуске приложения.
     */
    public static int toId(String rawId) {
        if (rawId == null || rawId.isEmpty()) {
            return 0;
        }
        return rawId.hashCode() & 0x7fffffff;
    }

    public static Team toDomain(LolTeamDto dto) {
        if (dto == null || dto.name == null) {
            return null;
        }
        return new Team(
                toId(dto.id),
                isEmpty(dto.code) ? dto.name : dto.code,
                dto.name,
                dto.homeLeague == null ? "" : value(dto.homeLeague.region),
                dto.homeLeague == null ? "" : value(dto.homeLeague.name),
                logoUrl(dto),
                0,
                playerNames(dto.players)
        );
    }

    public static List<Team> toDomainList(List<LolTeamDto> dtos) {
        List<Team> result = new ArrayList<>();
        if (dtos == null) {
            return result;
        }
        for (LolTeamDto dto : dtos) {
            Team team = toDomain(dto);
            if (team != null) {
                result.add(team);
            }
        }
        return result;
    }

    /** Команда из расписания — используется, если её нет в общем списке. */
    public static Team fromMatchTeam(LolMatchTeamDto dto) {
        if (dto == null || dto.name == null) {
            return null;
        }
        return new Team(
                toId(dto.name),
                isEmpty(dto.code) ? dto.name : dto.code,
                dto.name,
                "",
                "",
                normalizeUrl(dto.image),
                0,
                null
        );
    }

    /** HTTP-ссылки заменяются на HTTPS: Android по умолчанию блокирует cleartext. */
    public static String normalizeUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        return url.startsWith(HTTP) ? HTTPS + url.substring(HTTP.length()) : url;
    }

    private static String logoUrl(LolTeamDto dto) {
        String primary = normalizeUrl(dto.image);
        String alternative = normalizeUrl(dto.alternativeImage);
        if (primary != null && primary.contains(DEAD_LOGO_HOST)
                && alternative != null && !alternative.contains(DEAD_LOGO_HOST)) {
            return alternative;
        }
        if (primary == null) {
            return alternative;
        }
        return primary;
    }

    private static List<String> playerNames(List<LolPlayerDto> players) {
        List<String> names = new ArrayList<>();
        if (players == null) {
            return names;
        }
        for (LolPlayerDto player : players) {
            if (player == null) {
                continue;
            }
            String name = player.summonerName;
            if (isEmpty(name)) {
                name = (value(player.firstName) + " " + value(player.lastName)).trim();
            }
            if (!isEmpty(name)) {
                names.add(name);
            }
        }
        return names;
    }

    private static String value(String value) {
        return value == null ? "" : value;
    }

    private static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
