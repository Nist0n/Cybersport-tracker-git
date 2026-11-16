package ru.mirea.pavlovve.cybersporttracker.data.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Сборка Retrofit-клиента (паттерн «одиночка»).
 *
 * <p>OkHttp отвечает за транспорт: таймауты, повторные попытки, заголовки
 * и логирование. Retrofit добавляет к нему декларативные запросы и
 * конвертер JSON → Java-объекты (Gson).</p>
 */
public final class ApiClient {

    private static volatile LolEsportsApi api;

    private ApiClient() {
    }

    public static LolEsportsApi api() {
        LolEsportsApi local = api;
        if (local == null) {
            synchronized (ApiClient.class) {
                if (api == null) {
                    api = buildRetrofit().create(LolEsportsApi.class);
                }
                local = api;
            }
        }
        return local;
    }

    private static Retrofit buildRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .callTimeout(90, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                // Общий заголовок для всех запросов: ключ доступа и формат ответа.
                .addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                        .header("x-api-key", ApiConfig.API_KEY)
                        .header("Accept", "application/json")
                        .build()))
                .addInterceptor(logging)
                .build();

        return new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
