package ru.mirea.pavlovve.cybersporttracker.data.network;

/**
 * Настройки удалённого REST-сервиса.
 *
 * <p>В качестве источника данных используется официальное API киберспорта
 * League of Legends (Riot Games). Базовый адрес заканчивается на «/» — это
 * требование Retrofit: относительный путь из аннотации {@code @GET}
 * приклеивается к нему.</p>
 */
public final class ApiConfig {

    public static final String BASE_URL = "https://esports-api.lolesports.com/persisted/gw/";

    /** Публичный ключ, который использует официальный сайт lolesports.com. */
    public static final String API_KEY = "0TvQnueqKa5mxJntVWt0w4LpLfEkrV1Ta8rQBb9Z";

    /** Язык ответа (влияет на названия лиг и регионов). */
    public static final String LANGUAGE = "en-US";

    private ApiConfig() {
    }
}
