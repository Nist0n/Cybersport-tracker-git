package ru.mirea.pavlovve.retrofitapp.data;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Сборка Retrofit-клиента (одиночка).
 *
 * <p>OkHttp отвечает за транспорт и логирование, Retrofit — за разбор
 * аннотаций интерфейса, Gson — за JSON.</p>
 */
public final class ApiClient {

    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private static volatile TodoApi api;

    private ApiClient() {
    }

    public static TodoApi api() {
        TodoApi local = api;
        if (local == null) {
            synchronized (ApiClient.class) {
                if (api == null) {
                    api = buildRetrofit().create(TodoApi.class);
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
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /** Понятное пользователю описание сетевой ошибки. */
    public static String describe(Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            return "Нет подключения к интернету";
        }
        if (throwable instanceof SocketTimeoutException) {
            return "Сервер не ответил вовремя";
        }
        if (throwable instanceof IOException) {
            return "Ошибка сети: " + throwable.getMessage();
        }
        return "Неизвестная ошибка: " + throwable.getMessage();
    }
}
