package ru.mirea.pavlovve.cybersporttracker.presentation;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.data.auth.AuthDataSource;
import ru.mirea.pavlovve.cybersporttracker.data.auth.CompositeAuthDataSource;
import ru.mirea.pavlovve.cybersporttracker.data.auth.FirebaseAuthDataSource;
import ru.mirea.pavlovve.cybersporttracker.data.auth.MockAuthDataSource;
import ru.mirea.pavlovve.cybersporttracker.data.local.AppDatabase;
import ru.mirea.pavlovve.cybersporttracker.data.network.NetworkApi;
import ru.mirea.pavlovve.cybersporttracker.data.repository.FavoritesRepositoryImpl;
import ru.mirea.pavlovve.cybersporttracker.data.repository.LogoRecognitionRepositoryImpl;
import ru.mirea.pavlovve.cybersporttracker.data.repository.MatchRepositoryImpl;
import ru.mirea.pavlovve.cybersporttracker.data.repository.PredictionRepositoryImpl;
import ru.mirea.pavlovve.cybersporttracker.data.repository.TeamRepositoryImpl;
import ru.mirea.pavlovve.cybersporttracker.data.repository.TournamentRepositoryImpl;
import ru.mirea.pavlovve.cybersporttracker.data.repository.TournamentSubscriptionRepositoryImpl;
import ru.mirea.pavlovve.cybersporttracker.data.repository.UserRepositoryImpl;
import ru.mirea.pavlovve.cybersporttracker.data.source.TfliteLogoDataSource;
import ru.mirea.pavlovve.cybersporttracker.data.storage.ClientStorage;
import ru.mirea.pavlovve.cybersporttracker.data.storage.SharedPreferencesClientStorage;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.LogoRecognitionRepository;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.MatchRepository;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.PredictionRepository;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.TeamRepository;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.TournamentRepository;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.TournamentSubscriptionRepository;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.UserRepository;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.AddTeamToFavoritesUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.GetMatchesUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.GetPredictionHistoryUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.GetTeamByIdUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.GetTeamsUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.GetTournamentsUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.GetUserProfileUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.LoginUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.LogoutUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.MakePredictionUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.RecognizeLogoUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.RegisterUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.RemoveTeamFromFavoritesUseCase;
import ru.mirea.pavlovve.cybersporttracker.domain.usecase.SubscribeToTournamentUseCase;

public class AppContainer {

    private static volatile AppContainer instance;

    private final NetworkApi networkApi = new NetworkApi();
    private final TfliteLogoDataSource logoDataSource = new TfliteLogoDataSource();
    private final AuthDataSource authDataSource = new CompositeAuthDataSource(
            new FirebaseAuthDataSource(), new MockAuthDataSource(networkApi));

    private final ClientStorage clientStorage;
    private final AppDatabase database;

    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final FavoritesRepositoryImpl favoritesRepository;
    private final PredictionRepository predictionRepository;
    private final LogoRecognitionRepository logoRecognitionRepository;
    private final TournamentSubscriptionRepository subscriptionRepository;

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final LogoutUseCase logoutUseCase;
    private final GetTeamsUseCase getTeamsUseCase;
    private final GetTeamByIdUseCase getTeamByIdUseCase;
    private final GetTournamentsUseCase getTournamentsUseCase;
    private final GetMatchesUseCase getMatchesUseCase;
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final RecognizeLogoUseCase recognizeLogoUseCase;
    private final AddTeamToFavoritesUseCase addTeamToFavoritesUseCase;
    private final RemoveTeamFromFavoritesUseCase removeTeamFromFavoritesUseCase;
    private final MakePredictionUseCase makePredictionUseCase;
    private final GetPredictionHistoryUseCase getPredictionHistoryUseCase;
    private final SubscribeToTournamentUseCase subscribeToTournamentUseCase;

    private AppContainer(Context context) {
        Context appContext = context.getApplicationContext();

        this.clientStorage = new SharedPreferencesClientStorage(appContext);
        this.database = AppDatabase.getInstance(appContext);

        this.teamRepository = new TeamRepositoryImpl(networkApi);
        this.tournamentRepository = new TournamentRepositoryImpl(networkApi);
        this.matchRepository = new MatchRepositoryImpl(networkApi, teamRepository);
        this.userRepository = new UserRepositoryImpl(authDataSource, clientStorage);
        this.favoritesRepository =
                new FavoritesRepositoryImpl(database.favoriteTeamDao(), clientStorage);
        this.predictionRepository =
                new PredictionRepositoryImpl(database.predictionDao(), clientStorage);
        this.logoRecognitionRepository = new LogoRecognitionRepositoryImpl(logoDataSource);
        this.subscriptionRepository =
                new TournamentSubscriptionRepositoryImpl(database.subscriptionDao(), clientStorage);

        this.loginUseCase = new LoginUseCase(userRepository);
        this.registerUseCase = new RegisterUseCase(userRepository);
        this.logoutUseCase = new LogoutUseCase(userRepository);
        this.getTeamsUseCase = new GetTeamsUseCase(teamRepository);
        this.getTeamByIdUseCase = new GetTeamByIdUseCase(teamRepository);
        this.getTournamentsUseCase = new GetTournamentsUseCase(tournamentRepository);
        this.getMatchesUseCase = new GetMatchesUseCase(matchRepository);
        this.getUserProfileUseCase = new GetUserProfileUseCase(userRepository);
        this.recognizeLogoUseCase = new RecognizeLogoUseCase(logoRecognitionRepository);
        this.addTeamToFavoritesUseCase = new AddTeamToFavoritesUseCase(favoritesRepository);
        this.removeTeamFromFavoritesUseCase =
                new RemoveTeamFromFavoritesUseCase(favoritesRepository);
        this.makePredictionUseCase = new MakePredictionUseCase(predictionRepository);
        this.getPredictionHistoryUseCase = new GetPredictionHistoryUseCase(predictionRepository);
        this.subscribeToTournamentUseCase =
                new SubscribeToTournamentUseCase(subscriptionRepository);
    }

    public static AppContainer get(Context context) {
        if (instance == null) {
            synchronized (AppContainer.class) {
                if (instance == null) {
                    instance = new AppContainer(context);
                }
            }
        }
        return instance;
    }

    public LoginUseCase login() {
        return loginUseCase;
    }

    public RegisterUseCase register() {
        return registerUseCase;
    }

    public LogoutUseCase logout() {
        return logoutUseCase;
    }

    public GetTeamsUseCase teams() {
        return getTeamsUseCase;
    }

    public GetTeamByIdUseCase teamById() {
        return getTeamByIdUseCase;
    }

    public GetTournamentsUseCase tournaments() {
        return getTournamentsUseCase;
    }

    public GetMatchesUseCase matches() {
        return getMatchesUseCase;
    }

    public GetUserProfileUseCase profile() {
        return getUserProfileUseCase;
    }

    public RecognizeLogoUseCase logoRecognizer() {
        return recognizeLogoUseCase;
    }

    public AddTeamToFavoritesUseCase addToFavorites() {
        return addTeamToFavoritesUseCase;
    }

    public RemoveTeamFromFavoritesUseCase removeFromFavorites() {
        return removeTeamFromFavoritesUseCase;
    }

    public MakePredictionUseCase makePrediction() {
        return makePredictionUseCase;
    }

    public GetPredictionHistoryUseCase predictionHistory() {
        return getPredictionHistoryUseCase;
    }

    public SubscribeToTournamentUseCase subscribe() {
        return subscribeToTournamentUseCase;
    }

    public UserRepository userRepository() {
        return userRepository;
    }

    public LiveData<List<Team>> observeFavoriteTeams() {
        return favoritesRepository.observeFavoriteTeams();
    }

    public boolean isFirebaseNotConfigured() {
        return !authDataSource.isConfigured();
    }

    static String formatTeam(Team team) {
        return team == null ? "Команда не найдена" : team.getShortInfo();
    }
}
