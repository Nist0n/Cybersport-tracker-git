package ru.mirea.pavlovve.cybersporttracker.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.pavlovve.cybersporttracker.MainActivity;
import ru.mirea.pavlovve.cybersporttracker.R;
import ru.mirea.pavlovve.cybersporttracker.domain.models.User;

public class LoginActivity extends AppCompatActivity {

    private AppContainer container;
    private EditText loginInput;
    private EditText passwordInput;
    private TextView statusView;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        container = new AppContainer(this);
        loginInput = findViewById(R.id.editTextLogin);
        passwordInput = findViewById(R.id.editTextPassword);
        statusView = findViewById(R.id.textViewLoginStatus);

        if (container.isFirebaseNotConfigured()) {
            statusView.setText(R.string.login_mock_note);
        }

        Button signInButton = findViewById(R.id.buttonSignIn);
        signInButton.setOnClickListener(v -> signIn());
        Button signUpButton = findViewById(R.id.buttonSignUp);
        signUpButton.setOnClickListener(v -> signUp());

        if (container.profile().execute() != null) {
            openMainScreen();
        }
    }

    private void signIn() {
        String login = loginInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        if (login.isEmpty() || password.isEmpty()) {
            statusView.setText(R.string.login_empty_fields);
            return;
        }
        statusView.setText(R.string.login_in_progress);
        executor.execute(() -> {
            User user = container.login().execute(login, password);
            runOnUiThread(() -> {
                if (user == null) {
                    statusView.setText(R.string.login_error);
                } else {
                    openMainScreen();
                }
            });
        });
    }

    private void signUp() {
        String login = loginInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        if (login.isEmpty() || password.isEmpty()) {
            statusView.setText(R.string.login_empty_fields);
            return;
        }
        statusView.setText(R.string.login_in_progress);
        executor.execute(() -> {
            User user = container.register().execute(login, password);
            runOnUiThread(() -> {
                if (user == null) {
                    statusView.setText(R.string.register_error);
                } else {
                    openMainScreen();
                }
            });
        });
    }

    private void openMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
