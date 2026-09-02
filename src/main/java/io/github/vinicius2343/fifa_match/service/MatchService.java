package io.github.vinicius2343.fifa_match.service;

import io.github.vinicius2343.fifa_match.dto.*;
import io.github.vinicius2343.fifa_match.enums.FilterType;
import io.github.vinicius2343.fifa_match.enums.MatchSize;
import io.github.vinicius2343.fifa_match.enums.RatingOperator;
import io.github.vinicius2343.fifa_match.model.Team;
import io.github.vinicius2343.fifa_match.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MatchService {

    private final TeamRepository teamRepository;

    public MatchService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public MatchRandomizeResponse randomize(MatchRandomizeRequest request) {

        validatePlayers(request.players(), request.matchSize());

        List<Team> availableTeams = getAvailableTeams(request.filters());

        if (availableTeams.size() < 2) {
            throw new IllegalStateException("Não existem times suficientes para realizar o sorteio.");
        }

        Collections.shuffle(availableTeams);

        Team firstTeam = availableTeams.get(0);
        Team secondTeam = availableTeams.get(1);

        List<String> players = new ArrayList<>(request.players());

        Collections.shuffle(players);

        int playersPerTeam = switch (request.matchSize()) {
            case ONE_V_ONE -> 1;
            case TWO_V_TWO -> 2;
        };

        List<String> firstTeamPlayers = new ArrayList<>(players.subList(0, playersPerTeam));

        List<String> secondTeamPlayers = new ArrayList<>(players.subList(playersPerTeam, playersPerTeam * 2));

        List<String> playersOut = new ArrayList<>(players.subList(playersPerTeam * 2, players.size()));

        return new MatchRandomizeResponse(

                List.of(
                        new TeamResult(firstTeam.getName(), firstTeamPlayers),
                        new TeamResult(secondTeam.getName(), secondTeamPlayers)
                ),

                playersOut
        );
    }

    private List<Team> getAvailableTeams(List<MatchFilterRequest> filters) {

        if (filters == null || filters.isEmpty()) {
            return teamRepository.findAll();
        }

        List<Team> teams = teamRepository.findAll();

        return new ArrayList<>(teams.stream().filter(team -> matchesFilters(team, filters)).toList());
    }

    private boolean matchesFilters(Team team, List<MatchFilterRequest> filters) {

        return filters.stream().allMatch(filter -> matchesFilter(team, filter));
    }

    private boolean matchesFilter(Team team, MatchFilterRequest filter) {

        if (filter.teamType() != null && team.getType() != filter.teamType()) {
            return false;
        }

        if (filter.type() == null) {
            return true;
        }

        return switch (filter.type()) {

            case COUNTRY -> team.getCountry() != null && team.getCountry().equalsIgnoreCase(filter.value());

            case LEAGUE -> team.getLeague() != null && team.getLeague().equalsIgnoreCase(filter.value());

            case RATING -> matchesRating(team, filter);
        };
    }

    private boolean matchesRating(Team team, MatchFilterRequest filter) {

        double starsRequested;

        try {
            starsRequested = Double.parseDouble(filter.value());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Rating inválido: " + filter.value()
            );
        }

        double teamStars = convertRatingToStars(team.getRating());

        RatingOperator operator = filter.operator() == null
                        ? RatingOperator.LESS_THAN_OR_EQUAL
                        : RatingOperator.valueOf(filter.operator());

        return switch (operator) {

            case EQUALS -> teamStars == starsRequested;

            case LESS_THAN_OR_EQUAL -> teamStars <= starsRequested;

            case GREATER_THAN_OR_EQUAL -> teamStars >= starsRequested;
        };
    }

    private void validatePlayers(List<String> players, MatchSize matchSize) {

        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("É necessário informar os jogadores.");
        }

        if (matchSize == null) {
            throw new IllegalArgumentException("É necessário informar o modo da partida.");
        }

        int expectedPlayers = switch (matchSize) {
            case ONE_V_ONE -> 2;
            case TWO_V_TWO -> 4;
        };

        if (players.size() < expectedPlayers) {
            throw new IllegalArgumentException("O modo " + matchSize + " exige pelo menos " + expectedPlayers + " jogadores."
            );
        }
    }

    public FiltersResponse getFilters() {

        List<String> countries = teamRepository.findDistinctCountries();
        List<String> leagues = teamRepository.findDistinctLeagues();
        List<String> ratings = List.of("1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5", "5.0");

        List<FilterOptionResponse> teamFilters = List.of(
                        new FilterOptionResponse(FilterType.COUNTRY, "País", countries),
                        new FilterOptionResponse(FilterType.LEAGUE, "Liga", leagues),
                        new FilterOptionResponse(FilterType.RATING, "Força", ratings)
        );

        List<FilterOptionResponse> nationalTeamFilters = List.of(
                new FilterOptionResponse(FilterType.RATING, "Força", ratings)
        );

        return new FiltersResponse(teamFilters, nationalTeamFilters);
    }

    private double convertRatingToStars(Integer rating) {

        if (rating == null) {
            return 0;
        }

        if (rating >= 83) return 5.0;
        if (rating >= 79) return 4.5;
        if (rating >= 75) return 4.0;
        if (rating >= 71) return 3.5;
        if (rating >= 69) return 3.0;
        if (rating >= 67) return 2.5;
        if (rating >= 65) return 2.0;
        if (rating >= 63) return 1.5;
        if (rating >= 60) return 1.0;

        return 0.5;
    }
}