package ru.mirea.pavlovve.cybersporttracker.domain.repository;

import java.util.List;

import ru.mirea.pavlovve.cybersporttracker.domain.models.Match;

public interface MatchRepository {
    List<Match> getMatches();
    Match getMatchById(int id);
}
