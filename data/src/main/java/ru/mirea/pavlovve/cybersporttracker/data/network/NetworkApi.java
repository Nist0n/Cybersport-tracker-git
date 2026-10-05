package ru.mirea.pavlovve.cybersporttracker.data.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.dto.MatchDto;
import ru.mirea.pavlovve.cybersporttracker.data.dto.TeamDto;
import ru.mirea.pavlovve.cybersporttracker.data.dto.TournamentDto;
import ru.mirea.pavlovve.cybersporttracker.data.dto.UserDto;

public class NetworkApi {

    public static final String BASE_URL = "https://mockapi.io/api/v1/cybersport-tracker";

    private static final String TEAMS_JSON = "["
            + "{\"id\":1,\"tag\":\"NAVI\",\"name\":\"Natus Vincere\",\"country\":\"Украина\","
            + "\"logo_url\":\"https://mockapi.io/logos/navi.png\",\"world_rank\":1,"
            + "\"players\":[\"s1mple\",\"b1t\",\"jL\",\"Aleksib\",\"iM\"]},"
            + "{\"id\":2,\"tag\":\"Spirit\",\"name\":\"Team Spirit\",\"country\":\"Россия\","
            + "\"logo_url\":\"https://mockapi.io/logos/spirit.png\",\"world_rank\":2,"
            + "\"players\":[\"donk\",\"sh1ro\",\"chopper\",\"magixx\",\"zont1x\"]},"
            + "{\"id\":3,\"tag\":\"FaZe\",\"name\":\"FaZe Clan\",\"country\":\"США\","
            + "\"logo_url\":\"https://mockapi.io/logos/faze.png\",\"world_rank\":4,"
            + "\"players\":[\"rain\",\"karrigan\",\"broky\",\"frozen\",\"EliGE\"]},"
            + "{\"id\":4,\"tag\":\"G2\",\"name\":\"G2 Esports\",\"country\":\"Европа\","
            + "\"logo_url\":\"https://mockapi.io/logos/g2.png\",\"world_rank\":6,"
            + "\"players\":[\"huNter-\",\"malbsMd\",\"m0NESY\",\"Snax\",\"frozen\"]},"
            + "{\"id\":5,\"tag\":\"Vitality\",\"name\":\"Team Vitality\",\"country\":\"Франция\","
            + "\"logo_url\":\"https://mockapi.io/logos/vitality.png\",\"world_rank\":3,"
            + "\"players\":[\"ZywOo\",\"apEX\",\"flameZ\",\"ropz\",\"Spinx\"]}"
            + "]";

    private static final String TOURNAMENTS_JSON = "["
            + "{\"id\":1,\"name\":\"IEM Katowice 2026\",\"game\":\"CS2\","
            + "\"location\":\"Катовице, Польша\",\"start_date\":\"2026-02-02\","
            + "\"prize_pool\":\"$1,000,000\",\"status\":\"UPCOMING\"},"
            + "{\"id\":2,\"name\":\"PGL Major 2026\",\"game\":\"CS2\","
            + "\"location\":\"Стокгольм, Швеция\",\"start_date\":\"2026-05-10\","
            + "\"prize_pool\":\"$1,250,000\",\"status\":\"UPCOMING\"},"
            + "{\"id\":3,\"name\":\"The International 2026\",\"game\":\"Dota 2\","
            + "\"location\":\"Копенгаген, Дания\",\"start_date\":\"2026-08-15\","
            + "\"prize_pool\":\"$2,500,000\",\"status\":\"ANNOUNCED\"}"
            + "]";

    private static final String MATCHES_JSON = "["
            + "{\"id\":1,\"tournament_name\":\"IEM Katowice 2026\",\"home_team_id\":1,"
            + "\"away_team_id\":2,\"home_score\":0,\"away_score\":0,"
            + "\"start_time\":\"2026-02-03 18:00\",\"status\":\"SCHEDULED\"},"
            + "{\"id\":2,\"tournament_name\":\"IEM Katowice 2026\",\"home_team_id\":3,"
            + "\"away_team_id\":4,\"home_score\":0,\"away_score\":0,"
            + "\"start_time\":\"2026-02-03 21:00\",\"status\":\"SCHEDULED\"},"
            + "{\"id\":3,\"tournament_name\":\"PGL Major 2026\",\"home_team_id\":5,"
            + "\"away_team_id\":1,\"home_score\":13,\"away_score\":11,"
            + "\"start_time\":\"2026-05-11 20:00\",\"status\":\"FINISHED\"},"
            + "{\"id\":4,\"tournament_name\":\"PGL Major 2026\",\"home_team_id\":2,"
            + "\"away_team_id\":3,\"home_score\":16,\"away_score\":14,"
            + "\"start_time\":\"2026-05-11 23:00\",\"status\":\"FINISHED\"},"
            + "{\"id\":5,\"tournament_name\":\"PGL Major 2026\",\"home_team_id\":1,"
            + "\"away_team_id\":5,\"home_score\":7,\"away_score\":5,"
            + "\"start_time\":\"2026-05-12 19:00\",\"status\":\"LIVE\"}"
            + "]";

    private static final String USERS_JSON = "["
            + "{\"id\":1,\"login\":\"admin\",\"password\":\"1234\","
            + "\"nickname\":\"CyberTracker\",\"email\":\"admin@cybersport-tracker.ru\","
            + "\"avatar_url\":\"https://mockapi.io/avatars/1.png\"},"
            + "{\"id\":2,\"login\":\"user\",\"password\":\"user\","
            + "\"nickname\":\"viewer\",\"email\":\"viewer@cybersport-tracker.ru\","
            + "\"avatar_url\":\"https://mockapi.io/avatars/2.png\"}"
            + "]";

    private final Gson gson = new Gson();
    private final List<UserDto> users = new ArrayList<>();
    private int userIdSequence = 100;

    private List<TeamDto> teamsCache;
    private List<TournamentDto> tournamentsCache;
    private List<MatchDto> matchesCache;

    public NetworkApi() {
        List<UserDto> loaded = parse(USERS_JSON, new TypeToken<List<UserDto>>() {
        }.getType());
        if (loaded != null) {
            users.addAll(loaded);
            userIdSequence = loaded.size() + 1;
        }
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public List<TeamDto> getTeams() {
        if (teamsCache == null) {
            List<TeamDto> parsed = parse(TEAMS_JSON, new TypeToken<List<TeamDto>>() {
            }.getType());
            teamsCache = parsed == null ? new ArrayList<TeamDto>() : parsed;
        }
        return teamsCache;
    }

    public TeamDto getTeamById(int id) {
        for (TeamDto dto : getTeams()) {
            if (dto.id == id) {
                return dto;
            }
        }
        return null;
    }

    public List<TournamentDto> getTournaments() {
        if (tournamentsCache == null) {
            List<TournamentDto> parsed = parse(TOURNAMENTS_JSON,
                    new TypeToken<List<TournamentDto>>() {
                    }.getType());
            tournamentsCache = parsed == null ? new ArrayList<TournamentDto>() : parsed;
        }
        return tournamentsCache;
    }

    public TournamentDto getTournamentById(int id) {
        for (TournamentDto dto : getTournaments()) {
            if (dto.id == id) {
                return dto;
            }
        }
        return null;
    }

    public List<MatchDto> getMatches() {
        if (matchesCache == null) {
            List<MatchDto> parsed = parse(MATCHES_JSON, new TypeToken<List<MatchDto>>() {
            }.getType());
            matchesCache = parsed == null ? new ArrayList<MatchDto>() : parsed;
        }
        return matchesCache;
    }

    public MatchDto getMatchById(int id) {
        for (MatchDto dto : getMatches()) {
            if (dto.id == id) {
                return dto;
            }
        }
        return null;
    }

    public UserDto authenticate(String login, String password) {
        for (UserDto dto : users) {
            if (dto.login.equals(login) && dto.password.equals(password)) {
                return dto;
            }
        }
        return null;
    }

    public UserDto createUser(String login, String password) {
        for (UserDto existing : users) {
            if (existing.login.equals(login)) {
                return null;
            }
        }
        UserDto dto = new UserDto();
        dto.id = userIdSequence++;
        dto.login = login;
        dto.password = password;
        dto.nickname = login;
        dto.email = login + "@cybersport-tracker.ru";
        dto.avatar_url = "https://mockapi.io/avatars/" + dto.id + ".png";
        users.add(dto);
        return dto;
    }

    private <T> T parse(String json, Type type) {
        try {
            return gson.fromJson(json, type);
        } catch (RuntimeException e) {
            return null;
        }
    }
}
