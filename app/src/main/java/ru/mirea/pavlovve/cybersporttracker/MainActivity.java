package ru.mirea.pavlovve.cybersporttracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;
import ru.mirea.pavlovve.cybersporttracker.domain.models.User;
import ru.mirea.pavlovve.cybersporttracker.presentation.AppContainer;
import ru.mirea.pavlovve.cybersporttracker.presentation.LoginActivity;
import ru.mirea.pavlovve.cybersporttracker.presentation.teamdetails.TeamDetailsActivity;
import ru.mirea.pavlovve.cybersporttracker.presentation.teams.TeamsAdapter;
import ru.mirea.pavlovve.cybersporttracker.presentation.teams.TeamsUiState;
import ru.mirea.pavlovve.cybersporttracker.presentation.teams.TeamsViewModel;
import ru.mirea.pavlovve.cybersporttracker.presentation.teams.TeamsViewModelFactory;

public class MainActivity extends AppCompatActivity implements TeamsAdapter.Listener {

    private TeamsViewModel viewModel;
    private TeamsAdapter adapter;

    private RecyclerView recyclerView;
    private View loadingView;
    private View emptyView;
    private TextView emptyTitle;
    private TextView emptySubtitle;
    private View retryButton;
    private TextView countView;
    private MaterialCardView guestCard;
    private EditText searchInput;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View root = findViewById(R.id.mainRoot);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        AppContainer container = AppContainer.get(this);
        viewModel = new ViewModelProvider(this, new TeamsViewModelFactory(container))
                .get(TeamsViewModel.class);

        recyclerView = findViewById(R.id.recyclerTeams);
        loadingView = findViewById(R.id.loadingView);
        emptyView = findViewById(R.id.emptyView);
        emptyTitle = findViewById(R.id.textEmptyTitle);
        emptySubtitle = findViewById(R.id.textEmptySubtitle);
        retryButton = findViewById(R.id.buttonRetry);
        countView = findViewById(R.id.textTeamsCount);
        guestCard = findViewById(R.id.cardGuestHint);
        searchInput = findViewById(R.id.editSearch);

        adapter = new TeamsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        findViewById(R.id.buttonProfile).setOnClickListener(v -> showProfile());
        findViewById(R.id.buttonGuestLogin).setOnClickListener(v -> openLogin());
        retryButton.setOnClickListener(v -> viewModel.retry());

        setupSearch();
        setupFilters();

        viewModel.getState().observe(this, this::render);
        viewModel.getMessage().observe(this, messageRes ->
                Snackbar.make(root, messageRes, Snackbar.LENGTH_LONG).show());
        viewModel.getLoggedOut().observe(this, loggedOut -> {
            if (Boolean.TRUE.equals(loggedOut)) {
                openLogin();
            }
        });
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setQuery(s == null ? "" : s.toString());
            }
        });
    }

    private void setupFilters() {
        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                return;
            }
            int checkedId = checkedIds.get(0);
            if (checkedId == R.id.chipEmea) {
                viewModel.setCountry("EMEA");
            } else if (checkedId == R.id.chipKorea) {
                viewModel.setCountry("KOREA");
            } else if (checkedId == R.id.chipNorthAmerica) {
                viewModel.setCountry("NORTH AMERICA");
            } else if (checkedId == R.id.chipChina) {
                viewModel.setCountry("CHINA");
            } else {
                viewModel.setCountry("");
            }
        });
    }

    private void render(TeamsUiState state) {
        currentUser = state.getUser();
        adapter.submitList(state.getTeams());

        boolean hasError = state.hasError();
        boolean showList = !state.isLoading() && !hasError && !state.isEmpty();
        recyclerView.setVisibility(showList ? View.VISIBLE : View.INVISIBLE);
        loadingView.setVisibility(state.isLoading() ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(!state.isLoading() && (state.isEmpty() || hasError)
                ? View.VISIBLE
                : View.GONE);
        guestCard.setVisibility(state.isAuthorized() ? View.GONE : View.VISIBLE);

        if (hasError) {
            emptyTitle.setText(R.string.teams_error_title);
            emptySubtitle.setText(R.string.teams_error_subtitle);
            retryButton.setVisibility(View.VISIBLE);
        } else {
            emptyTitle.setText(R.string.teams_empty_title);
            emptySubtitle.setText(R.string.teams_empty_subtitle);
            retryButton.setVisibility(View.GONE);
        }

        if (state.isLoading()) {
            countView.setText(R.string.teams_loading);
        } else if (hasError) {
            countView.setText(R.string.teams_error_count);
        } else if (state.hasFilter()) {
            countView.setText(getString(R.string.teams_found, state.getFoundCount()));
        } else {
            countView.setText(getString(R.string.teams_total, state.getTotalCount()));
        }
    }

    private void showProfile() {
        if (currentUser == null) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.profile_guest_title)
                    .setMessage(R.string.profile_guest_message)
                    .setPositiveButton(R.string.btn_sign_in, (dialog, which) -> openLogin())
                    .setNegativeButton(R.string.action_close, null)
                    .show();
            return;
        }
        new MaterialAlertDialogBuilder(this)
                .setTitle(currentUser.getNickname())
                .setMessage(currentUser.getEmail() + "\n\n" + getString(R.string.profile_role))
                .setPositiveButton(R.string.btn_logout, (dialog, which) -> viewModel.logout())
                .setNegativeButton(R.string.action_close, null)
                .show();
    }

    private void openLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onTeamClick(Team team) {
        Intent intent = new Intent(this, TeamDetailsActivity.class);
        intent.putExtra(TeamDetailsActivity.EXTRA_TEAM_ID, team.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Team team) {
        viewModel.toggleFavorite(team);
    }
}
