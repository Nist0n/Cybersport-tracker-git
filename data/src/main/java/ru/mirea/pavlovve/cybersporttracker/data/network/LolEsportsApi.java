package ru.mirea.pavlovve.cybersporttracker.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.mirea.pavlovve.cybersporttracker.data.dto.LolLeaguesResponse;
import ru.mirea.pavlovve.cybersporttracker.data.dto.LolScheduleResponse;
import ru.mirea.pavlovve.cybersporttracker.data.dto.LolTeamsResponse;

/**
 * Описание REST-запросов в виде Java-интерфейса.
 *
 * <p>Retrofit во время выполнения создаёт реализацию этого интерфейса:
 * аннотация задаёт HTTP-метод и путь, {@code @Query} — query-параметр,
 * а возвращаемый тип {@code Call<T>} описывает отложенный запрос.</p>
 */
public interface LolEsportsApi {

    /** GET getTeams — список всех команд (вместе с составом). */
    @GET("getTeams")
    Call<LolTeamsResponse> getTeams(@Query("hl") String language);

    /** GET getLeagues — список лиг (в приложении отображаются как турниры). */
    @GET("getLeagues")
    Call<LolLeaguesResponse> getLeagues(@Query("hl") String language);

    /** GET getSchedule — расписание матчей. */
    @GET("getSchedule")
    Call<LolScheduleResponse> getSchedule(@Query("hl") String language);
}
