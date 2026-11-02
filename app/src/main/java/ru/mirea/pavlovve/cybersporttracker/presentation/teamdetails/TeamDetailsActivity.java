package ru.mirea.pavlovve.cybersporttracker.presentation.teamdetails;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

import ru.mirea.pavlovve.cybersporttracker.R;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Match;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;
import ru.mirea.pavlovve.cybersporttracker.presentation.AppContainer;
import ru.mirea.pavlovve.cybersporttracker.presentation.LoginActivity;
import ru.mirea.pavlovve.cybersporttracker.presentation.common.TeamLogoResolver;

public class TeamDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_TEAM_ID = "extra_team_id";

    private TeamDetailsViewModel viewModel;
    private ScrollViewState state;

    private View contentView;
    private CircularProgressIndicator progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_team_details);

        View root = findViewById(R.id.detailsRoot);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        int teamId = getIntent().getIntExtra(EXTRA_TEAM_ID, -1);
        if (teamId <= 0) {
            finish();
            return;
        }

        AppContainer container = AppContainer.get(this);
        viewModel = new ViewModelProvider(this, new TeamDetailsViewModelFactory(container, teamId))
                .get(TeamDetailsViewModel.class);

        contentView = findViewById(R.id.detailsContent);
        progressView = findViewById(R.id.progressDetails);
        state = new ScrollViewState();

        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
        findViewById(R.id.buttonFavoriteDetails).setOnClickListener(v -> {
            if (state.authorized) {
                viewModel.toggleFavorite();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        viewModel.getState().observe(this, this::render);
        viewModel.getMessage().observe(this, messageRes ->
                Snackbar.make(root, messageRes, Snackbar.LENGTH_LONG).show());
    }

    private void render(TeamDetailsUiState uiState) {
        progressView.setVisibility(uiState.isLoading() ? View.VISIBLE : View.GONE);
        contentView.setVisibility(uiState.isLoading() ? View.INVISIBLE : View.VISIBLE);

        if (uiState.isNotFound()) {
            finish();
            return;
        }
        Team team = uiState.getTeam();
        if (team == null) {
            return;
        }

        state.authorized = uiState.isAuthorized();

        ((ImageView) findViewById(R.id.imageDetailLogo))
                .setImageResource(TeamLogoResolver.resolve(team));
        ((TextView) findViewById(R.id.textDetailTag)).setText(team.getTag());
        ((TextView) findViewById(R.id.textDetailName)).setText(team.getName());
        ((TextView) findViewById(R.id.textDetailMeta)).setText(
                String.format(Locale.getDefault(), "%s · место в рейтинге %d",
                        team.getCountry(), team.getWorldRank()));

        bindPlayers(team);
        bindMatches(uiState.getMatches());
        bindInfo(team);
        bindFavoriteButton(uiState);
    }

    private void bindPlayers(Team team) {
        LinearLayout container = findViewById(R.id.playersContainer);
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        List<String> players = team.getPlayers();
        if (players.isEmpty()) {
            addEmptyRow(container, R.string.team_players_empty);
            return;
        }
        for (int i = 0; i < players.size(); i++) {
            View row = inflater.inflate(R.layout.item_player, container, false);
            ((TextView) row.findViewById(R.id.textPlayerIndex))
                    .setText(String.valueOf(i + 1));
            ((TextView) row.findViewById(R.id.textPlayerNickname))
                    .setText(players.get(i));
            container.addView(row);
        }
    }

    private void bindMatches(List<Match> matches) {
        LinearLayout container = findViewById(R.id.matchesContainer);
        container.removeAllViews();
        if (matches.isEmpty()) {
            addEmptyRow(container, R.string.team_matches_empty);
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        for (Match match : matches) {
            View row = inflater.inflate(R.layout.item_match_compact, container, false);
            ((TextView) row.findViewById(R.id.textMatchTitle)).setText(
                    match.getHomeTeam().getTag() + " vs " + match.getAwayTeam().getTag());
            String score = Match.STATUS_SCHEDULED.equals(match.getStatus())
                    ? match.getStartTime()
                    : match.getHomeScore() + ":" + match.getAwayScore();
            ((TextView) row.findViewById(R.id.textMatchMeta)).setText(
                    String.format(Locale.getDefault(), "%s · %s · %s",
                            match.getTournamentName(), score, match.getStatus()));
            container.addView(row);
        }
    }

    private void bindInfo(Team team) {
        LinearLayout container = findViewById(R.id.infoContainer);
        container.removeAllViews();
        addInfoRow(container, R.drawable.ic_flag, R.string.team_info_country, team.getCountry());
        addInfoRow(container, R.drawable.ic_rank, R.string.team_info_rank,
                String.valueOf(team.getWorldRank()));
        addInfoRow(container, R.drawable.ic_image, R.string.team_info_logo,
                getString(R.string.team_info_logo_value));
    }

    private void bindFavoriteButton(TeamDetailsUiState uiState) {
        MaterialButton button = findViewById(R.id.buttonFavoriteDetails);
        if (!uiState.isAuthorized()) {
            button.setText(R.string.teams_login_hint);
            button.setIconResource(R.drawable.ic_lock);
            button.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.surface_elevated)));
            button.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            button.setIconTint(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.text_secondary)));
            button.setStrokeColor(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.border)));
            button.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.stroke_width));
            return;
        }
        if (uiState.isFavorite()) {
            button.setText(R.string.team_in_favorites);
            button.setIconResource(R.drawable.ic_favorite);
            button.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.surface_elevated)));
            button.setTextColor(ContextCompat.getColor(this, R.color.primary));
            button.setIconTint(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.primary)));
            button.setStrokeColor(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.primary)));
            button.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.stroke_width));
        } else {
            button.setText(R.string.team_add_favorite);
            button.setIconResource(R.drawable.ic_favorite_border);
            button.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.primary)));
            button.setTextColor(ContextCompat.getColor(this, R.color.white));
            button.setIconTint(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.white)));
            button.setStrokeWidth(0);
        }
    }

    private void addInfoRow(LinearLayout container, int iconRes, int labelRes, String value) {
        View row = LayoutInflater.from(this).inflate(R.layout.item_info_row, container, false);
        ((ImageView) row.findViewById(R.id.imageInfoIcon)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.textInfoLabel)).setText(labelRes);
        ((TextView) row.findViewById(R.id.textInfoValue)).setText(value);
        container.addView(row);
    }

    private void addEmptyRow(LinearLayout container, int textRes) {
        TextView textView = new TextView(this);
        textView.setText(textRes);
        textView.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        textView.setTextSize(14);
        int padding = getResources().getDimensionPixelSize(R.dimen.spacing_12);
        textView.setPadding(0, padding, 0, padding);
        container.addView(textView);
    }

    private static final class ScrollViewState {
        private boolean authorized;
    }
}
