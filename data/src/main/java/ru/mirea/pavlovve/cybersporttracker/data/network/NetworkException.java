package ru.mirea.pavlovve.cybersporttracker.data.network;

/**
 * Ошибка сетевого слоя: нет соединения, таймаут, некорректный ответ
 * или HTTP-код 4xx/5xx.
 */
public class NetworkException extends RuntimeException {

    /** HTTP-код ответа; 0 — ошибка соединения или чтения данных. */
    private final int httpCode;

    public NetworkException(String message, int httpCode, Throwable cause) {
        super(message, cause);
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public boolean isHttpError() {
        return httpCode > 0;
    }
}
