package ru.mirea.pavlovve.cybersporttracker.domain.models;

public class User {
    private final int id;
    private final String login;
    private final String nickname;
    private final String email;
    private final String avatarUrl;

    public User(int id, String login, String nickname, String email, String avatarUrl) {
        this.id = id;
        this.login = login;
        this.nickname = nickname;
        this.email = email;
        this.avatarUrl = avatarUrl;
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

    public String getShortInfo() {
        return nickname + " (" + login + ") — " + email;
    }

    @Override
    public String toString() {
        return getShortInfo();
    }
}
