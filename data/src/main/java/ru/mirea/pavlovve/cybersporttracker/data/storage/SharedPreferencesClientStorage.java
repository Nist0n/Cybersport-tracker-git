package ru.mirea.pavlovve.cybersporttracker.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.pavlovve.cybersporttracker.data.storage.model.ClientInfo;

public class SharedPreferencesClientStorage implements ClientStorage {

    private static final String SHARED_PREFS_NAME = "client_prefs";
    private static final String KEY_ID = "client_id";
    private static final String KEY_LOGIN = "client_login";
    private static final String KEY_NICKNAME = "client_nickname";
    private static final String KEY_EMAIL = "client_email";
    private static final String KEY_AVATAR = "client_avatar";
    private static final String KEY_SAVED_AT = "client_saved_at";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesClientStorage(Context context) {
        this.sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public ClientInfo get() {
        String login = sharedPreferences.getString(KEY_LOGIN, null);
        if (login == null) {
            return null;
        }
        return new ClientInfo(
                sharedPreferences.getInt(KEY_ID, -1),
                login,
                sharedPreferences.getString(KEY_NICKNAME, login),
                sharedPreferences.getString(KEY_EMAIL, ""),
                sharedPreferences.getString(KEY_AVATAR, ""),
                sharedPreferences.getString(KEY_SAVED_AT, "")
        );
    }

    @Override
    public boolean save(ClientInfo info) {
        if (info == null || info.getLogin() == null) {
            return false;
        }
        return sharedPreferences.edit()
                .putInt(KEY_ID, info.getId())
                .putString(KEY_LOGIN, info.getLogin())
                .putString(KEY_NICKNAME, info.getNickname())
                .putString(KEY_EMAIL, info.getEmail())
                .putString(KEY_AVATAR, info.getAvatarUrl())
                .putString(KEY_SAVED_AT, info.getSavedAt())
                .commit();
    }

    @Override
    public boolean clear() {
        return sharedPreferences.edit().clear().commit();
    }
}
