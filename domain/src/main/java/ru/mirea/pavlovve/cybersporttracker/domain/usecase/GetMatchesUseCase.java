package ru.mirea.pavlovve.cybersporttracker.domain.usecase;

import java.util.Collections;
import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Match;
import ru.mirea.pavlovve.cybersporttracker.domain.repository.MatchRepository;

public class GetMatchesUseCase {
    private final MatchRepository matchRepository;

    public GetMatchesUseCase(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> execute() {
        List<Match> matches = matchRepository.getMatches();
        return matches == null ? Collections.<Match>emptyList() : matches;
    }

    public Match execute(int matchId) {
        if (matchId <= 0) {
            return null;
        }
        return matchRepository.getMatchById(matchId);
    }
}
