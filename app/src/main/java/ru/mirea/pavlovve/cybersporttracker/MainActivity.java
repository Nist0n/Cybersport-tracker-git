package ru.mirea.pavlovve.cybersporttracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Locale;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Match;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Prediction;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Tournament;
import ru.mirea.pavlovve.cybersporttracker.domain.models.User;
import ru.mirea.pavlovve.cybersporttracker.presentation.AppContainer;
import ru.mirea.pavlovve.cybersporttracker.presentation.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private AppContainer container;
    private TextView resultView;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        container = new AppContainer(this);
        resultView = findViewById(R.id.textViewResult);

        currentUser = container.profile().execute();

        bind(R.id.buttonLogout, v -> logout());
        bind(R.id.buttonProfile, v -> showProfile());
        bind(R.id.buttonTeams, v -> showTeams());
        bind(R.id.buttonSearch, v -> searchTeams());
        bind(R.id.buttonTeamDetails, v -> showTeamDetails());
        bind(R.id.buttonTournaments, v -> showTournaments());
        bind(R.id.buttonMatches, v -> showMatches());
        bind(R.id.buttonFavorite, v -> addFavorite());
        bind(R.id.buttonSubscribe, v -> subscribe());
        bind(R.id.buttonPrediction, v -> makePrediction());
        bind(R.id.buttonHistory, v -> showHistory());
        bind(R.id.buttonLogo, v -> recognizeLogo());

        showInfo();
    }

    private void bind(int buttonId, android.view.View.OnClickListener listener) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(listener);
    }

    private void showInfo() {
        resultView.setText(getString(R.string.demo_info,
                container.teams().execute().size(),
                container.tournaments().execute().size(),
                container.matches().execute().size())
                + "\n" + getString(R.string.session_info,
                AppContainer.formatUser(currentUser)));
    }

    private void logout() {
        container.userRepository().logout();
        currentUser = null;
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showProfile() {
        User user = container.profile().execute();
        currentUser = user;
        resultView.setText(getString(R.string.profile_header,
                AppContainer.formatUser(user)));
    }

    private void showTeams() {
        List<Team> teams = container.teams().execute();
        resultView.setText(getString(R.string.teams_header, teams.size())
                + "\n" + AppContainer.formatTeams(teams));
    }

    private void searchTeams() {
        List<Team> teams = container.teams().execute("Россия");
        resultView.setText(getString(R.string.search_header, teams.size())
                + "\n" + AppContainer.formatTeams(teams));
    }

    private void showTeamDetails() {
        Team team = container.teamById().execute(1);
        if (team == null) {
            resultView.setText(R.string.team_not_found);
            return;
        }
        resultView.setText(getString(R.string.team_header, team.getName())
                + "\n" + team.getShortInfo()
                + "\n" + getString(R.string.team_players, team.getPlayers().toString())
                + "\n" + team.getLogoUrl());
    }

    private void showTournaments() {
        List<Tournament> tournaments = container.tournaments().execute();
        resultView.setText(getString(R.string.tournaments_header, tournaments.size())
                + "\n" + AppContainer.formatTournaments(tournaments));
    }

    private void showMatches() {
        List<Match> matches = container.matches().execute();
        resultView.setText(getString(R.string.matches_header, matches.size())
                + "\n" + AppContainer.formatMatches(matches));
    }

    private boolean ensureAuthorized() {
        if (currentUser == null) {
            resultView.setText(R.string.need_login);
            return false;
        }
        return true;
    }

    private void addFavorite() {
        if (!ensureAuthorized()) {
            return;
        }
        Team team = container.teamById().execute(2);
        boolean added = container.addToFavorites().execute(team);
        resultView.setText(added
                ? getString(R.string.favorite_added, team.getShortInfo())
                : getString(R.string.favorite_exists, team.getShortInfo()));
    }

    private void subscribe() {
        if (!ensureAuthorized()) {
            return;
        }
        Tournament tournament = container.tournaments().execute(1);
        boolean subscribed = container.subscribe().execute(tournament);
        resultView.setText(subscribed
                ? getString(R.string.subscribe_done, tournament.getName())
                : getString(R.string.subscribe_exists, tournament.getName()));
    }

    private void makePrediction() {
        if (!ensureAuthorized()) {
            return;
        }
        Match match = container.matches().execute(1);
        Team team = match.getHomeTeam();
        Prediction prediction = new Prediction(
                0,
                match.getId(),
                team.getTag(),
                "2:1",
                1.5,
                Prediction.STATUS_PENDING
        );
        boolean accepted = container.makePrediction().execute(prediction);
        resultView.setText(accepted
                ? getString(R.string.prediction_done, team.getTag(), match.getShortInfo())
                : getString(R.string.prediction_failed));
    }

    private void showHistory() {
        if (!ensureAuthorized()) {
            return;
        }
        List<Prediction> history = container.predictionHistory().execute();
        if (history.isEmpty()) {
            resultView.setText(R.string.history_empty);
            return;
        }
        StringBuilder builder = new StringBuilder(getString(R.string.history_header,
                history.size()));
        for (Prediction prediction : history) {
            builder.append("\n• ").append(prediction.getShortInfo());
        }
        resultView.setText(builder.toString());
    }

    private void recognizeLogo() {
        if (!ensureAuthorized()) {
            return;
        }
        String teamTag = container.logoRecognizer()
                .execute("content://gallery/logos/navi_logo.png");
        resultView.setText(teamTag == null
                ? getString(R.string.logo_failed)
                : String.format(Locale.ROOT, getString(R.string.logo_done), teamTag));
    }
}
