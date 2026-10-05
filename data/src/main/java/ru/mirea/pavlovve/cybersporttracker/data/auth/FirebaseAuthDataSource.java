package ru.mirea.pavlovve.cybersporttracker.data.auth;

import android.net.Uri;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ru.mirea.pavlovve.cybersporttracker.data.dto.UserDto;

public class FirebaseAuthDataSource implements AuthDataSource {

    private static final long TIMEOUT_SECONDS = 15;

    @Override
    public boolean isConfigured() {
        try {
            FirebaseAuth.getInstance();
            return true;
        } catch (Throwable notInitialized) {
            return false;
        }
    }

    @Override
    public UserDto signIn(String login, String password) {
        if (!isConfigured()) {
            return null;
        }
        try {
            AuthResult result = Tasks.await(
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(login, password),
                    TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return toDto(result.getUser(), password);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserDto signUp(String login, String password) {
        if (!isConfigured()) {
            return null;
        }
        try {
            AuthResult result = Tasks.await(
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(login, password),
                    TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return toDto(result.getUser(), password);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void signOut() {
        if (isConfigured()) {
            FirebaseAuth.getInstance().signOut();
        }
    }

    private UserDto toDto(FirebaseUser user, String password) {
        if (user == null) {
            return null;
        }
        String email = user.getEmail() == null ? "" : user.getEmail();
        UserDto dto = new UserDto();
        dto.id = Math.abs(Objects.hashCode(user.getUid()));
        dto.login = email;
        dto.password = password;
        String displayName = user.getDisplayName();
        dto.nickname = displayName == null || displayName.isEmpty()
                ? email.substring(0, email.indexOf('@') > 0 ? email.indexOf('@') : email.length())
                : displayName;
        dto.email = email;
        Uri photo = user.getPhotoUrl();
        dto.avatar_url = photo == null ? "" : photo.toString();
        return dto;
    }
}
