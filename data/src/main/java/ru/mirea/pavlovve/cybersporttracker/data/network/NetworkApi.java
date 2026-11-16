package ru.mirea.pavlovve.cybersporttracker.data.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.mirea.pavlovve.cybersporttracker.data.dto.LolEventDto;
import ru.mirea.pavlovve.cybersporttracker.data.dto.LolLeagueDto;
import ru.mirea.pavlovve.cybersporttracker.data.dto.LolLeaguesResponse;
import ru.mirea.pavlovve.cybersporttracker.data.dto.LolScheduleResponse;
import ru.mirea.pavlovve.cybersporttracker.data.dto.LolTeamDto;
import ru.mirea.pavlovve.cybersporttracker.data.dto.LolTeamsResponse;

/**
 * Фасад над Retrofit-интерфейсом.
 *
 * <p>Репозитории вызывают обычные Java-методы и получают готовые списки DTO.
 * Внутри выполняется синхронный {@code Call.execute()} — поэтому методы
 * обязаны вызываться из фонового потока (в приложении это ExecutorService
 * внутри ViewModel). Все сетевые ошибки приводятся к {@link NetworkException}.</p>
 *
 * <p>Ответы кэшируются в памяти: список команд весит около 1.5 МБ, и повторно
 * скачивать его при открытии карточки команды не нужно.</p>
 */
public class NetworkApi {

    private final LolEsportsApi api;

    private volatile List<LolTeamDto> teamsCache;
    private volatile List<LolLeagueDto> leaguesCache;
    private volatile List<LolEventDto> scheduleCache;

    public NetworkApi() {
        this(ApiClient.api());
    }

    public NetworkApi(LolEsportsApi api) {
        this.api = api;
    }

    public List<LolTeamDto> getTeams() {
        List<LolTeamDto> cached = teamsCache;
        if (cached == null) {
            synchronized (this) {
                cached = teamsCache;
                if (cached == null) {
                    LolTeamsResponse response = execute(
                            api.getTeams(ApiConfig.LANGUAGE), "getTeams");
                    List<LolTeamDto> teams = response.data == null
                            ? null
                            : response.data.teams;
                    cached = teams == null
                            ? Collections.<LolTeamDto>emptyList()
                            : Collections.unmodifiableList(new ArrayList<>(teams));
                    teamsCache = cached;
                }
            }
        }
        return cached;
    }

    public List<LolLeagueDto> getLeagues() {
        List<LolLeagueDto> cached = leaguesCache;
        if (cached == null) {
            synchronized (this) {
                cached = leaguesCache;
                if (cached == null) {
                    LolLeaguesResponse response = execute(
                            api.getLeagues(ApiConfig.LANGUAGE), "getLeagues");
                    List<LolLeagueDto> leagues = response.data == null
                            ? null
                            : response.data.leagues;
                    cached = leagues == null
                            ? Collections.<LolLeagueDto>emptyList()
                            : Collections.unmodifiableList(new ArrayList<>(leagues));
                    leaguesCache = cached;
                }
            }
        }
        return cached;
    }

    public List<LolEventDto> getSchedule() {
        List<LolEventDto> cached = scheduleCache;
        if (cached == null) {
            synchronized (this) {
                cached = scheduleCache;
                if (cached == null) {
                    LolScheduleResponse response = execute(
                            api.getSchedule(ApiConfig.LANGUAGE), "getSchedule");
                    List<LolEventDto> events = response.data == null
                            || response.data.schedule == null
                            ? null
                            : response.data.schedule.events;
                    cached = events == null
                            ? Collections.<LolEventDto>emptyList()
                            : Collections.unmodifiableList(new ArrayList<>(events));
                    scheduleCache = cached;
                }
            }
        }
        return cached;
    }

    /** Сбрасывает кэш — используется кнопкой «Повторить» после ошибки. */
    public synchronized void clearCache() {
        teamsCache = null;
        leaguesCache = null;
        scheduleCache = null;
    }

    private <T> T execute(Call<T> call, String endpoint) {
        try {
            Response<T> response = call.execute();
            if (!response.isSuccessful()) {
                throw new NetworkException(
                        "HTTP " + response.code() + " при запросе " + endpoint,
                        response.code(), null);
            }
            T body = response.body();
            if (body == null) {
                throw new NetworkException(
                        "Пустой ответ " + endpoint, response.code(), null);
            }
            return body;
        } catch (IOException e) {
            throw new NetworkException(
                    "Нет соединения с сервером (" + endpoint + "): " + e.getMessage(),
                    0, e);
        }
    }
}
