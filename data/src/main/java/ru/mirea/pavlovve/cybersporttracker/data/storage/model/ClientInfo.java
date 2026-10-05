package ru.mirea.pavlovve.cybersporttracker.data.storage.model;

public class ClientInfo {
    private final int id;
    private final String login;
    private final String nickname;
    private final String email;
    private final String avatarUrl;
    private final String savedAt;

    public ClientInfo(int id, String login, String nickname,
                      String email, String avatarUrl, String savedAt) {
        this.id = id;
        this.login = login;
        this.nickname = nickname;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.savedAt = savedAt;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getSavedAt() {
        return savedAt;
    }
}
