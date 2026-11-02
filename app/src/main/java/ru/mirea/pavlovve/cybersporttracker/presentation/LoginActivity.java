package ru.mirea.pavlovve.cybersporttracker.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ru.mirea.pavlovve.cybersporttracker.MainActivity;
import ru.mirea.pavlovve.cybersporttracker.R;
import ru.mirea.pavlovve.cybersporttracker.presentation.login.LoginUiState;
import ru.mirea.pavlovve.cybersporttracker.presentation.login.LoginViewModel;
import ru.mirea.pavlovve.cybersporttracker.presentation.login.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextView statusView;
    private MaterialButton signInButton;
    private MaterialButton signUpButton;
    private CircularProgressIndicator progressView;
    private boolean navigated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        View root = findViewById(R.id.loginRoot);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        AppContainer container = AppContainer.get(this);
        viewModel = new ViewModelProvider(this, new LoginViewModelFactory(container))
                .get(LoginViewModel.class);

        emailInput = findViewById(R.id.editTextLogin);
        passwordInput = findViewById(R.id.editTextPassword);
        emailLayout = findViewById(R.id.layoutEmail);
        passwordLayout = findViewById(R.id.layoutPassword);
        statusView = findViewById(R.id.textViewLoginStatus);
        signInButton = findViewById(R.id.buttonSignIn);
        signUpButton = findViewById(R.id.buttonSignUp);
        progressView = findViewById(R.id.progressLogin);

        if (container.isFirebaseNotConfigured()) {
            statusView.setText(R.string.login_mock_note);
            statusView.setVisibility(View.VISIBLE);
        }

        signInButton.setOnClickListener(v -> submit(false));
        signUpButton.setOnClickListener(v -> submit(true));
        findViewById(R.id.buttonSkip).setOnClickListener(v -> openMainScreen());

        passwordInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit(false);
                return true;
            }
            return false;
        });

        viewModel.getState().observe(this, this::render);
    }

    private void submit(boolean registration) {
        String email = emailInput.getText() == null ? "" : emailInput.getText().toString();
        String password = passwordInput.getText() == null
                ? ""
                : passwordInput.getText().toString();
        emailLayout.setError(null);
        passwordLayout.setError(null);
        if (registration) {
            viewModel.signUp(email, password);
        } else {
            viewModel.signIn(email, password);
        }
    }

    private void render(LoginUiState state) {
        boolean loading = state.isLoading();
        progressView.setVisibility(loading ? View.VISIBLE : View.GONE);
        signInButton.setEnabled(!loading);
        signUpButton.setEnabled(!loading);
        signInButton.setText(loading
                ? R.string.login_in_progress
                : R.string.btn_sign_in);

        if (state.getError() != LoginUiState.Error.NONE) {
            statusView.setVisibility(View.VISIBLE);
            statusView.setText(errorText(state.getError()));
        }

        if (state.getUser() != null && !navigated) {
            openMainScreen();
        }
    }

    private void openMainScreen() {
        if (navigated) {
            return;
        }
        navigated = true;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private int errorText(LoginUiState.Error error) {
        switch (error) {
            case EMPTY_FIELDS:
                return R.string.login_empty_fields;
            case LOGIN_FAILED:
                return R.string.login_error;
            case REGISTER_FAILED:
                return R.string.register_error;
            default:
                return R.string.login_mock_note;
        }
    }
}
