package io.github.vinicius2343.fifa_match.service;

import io.github.vinicius2343.fifa_match.dto.*;
import io.github.vinicius2343.fifa_match.enums.FilterType;
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

    public MatchRandomizeResponse randomize(
            MatchRandomizeRequest request
    ) {

        validatePlayers(request.players());

        List<Team> availableTeams =
                getAvailableTeams(request.filters());

        if (availableTeams.size() < 2) {
            throw new IllegalStateException(
                    "Não existem times suficientes para realizar o sorteio."
            );
        }

        Collections.shuffle(availableTeams);

        Team firstTeam = availableTeams.get(0);
        Team secondTeam = availableTeams.get(1);

        List<String> players =
                new ArrayList<>(request.players());

        Collections.shuffle(players);

        int playersPerTeam = players.size() / 2;

        List<String> firstTeamPlayers =
                new ArrayList<>(
                        players.subList(
                                0,
                                playersPerTeam
                        )
                );

        List<String> secondTeamPlayers =
                new ArrayList<>(
                        players.subList(
                                playersPerTeam,
                                playersPerTeam * 2
                        )
                );

        List<String> playersOut =
                new ArrayList<>(
                        players.subList(
                                playersPerTeam * 2,
                                players.size()
                        )
                );

        return new MatchRandomizeResponse(

                new ArrayList<>(Arrays.asList(
                        new TeamResult(
                                firstTeam.getName(),
                                firstTeamPlayers
                        ),
                        new TeamResult(
                                secondTeam.getName(),
                                secondTeamPlayers
                        )
                )),

                playersOut
        );
    }

    private List<Team> getAvailableTeams(
            List<MatchFilterRequest> filters
    ) {

        if (filters == null || filters.isEmpty()) {
            return teamRepository.findAll();
        }

        List<Team> teams = teamRepository.findAll();

        return new ArrayList<>(
                teams.stream()
                        .filter(team -> matchesFilters(team, filters))
                        .toList()
        );
    }

    private boolean matchesFilters(
            Team team,
            List<MatchFilterRequest> filters
    ) {

        return filters.stream()
                .allMatch(filter ->
                        matchesFilter(team, filter)
                );
    }

    private boolean matchesFilter(
            Team team,
            MatchFilterRequest filter
    ) {

        if (filter.teamType() != null
                && team.getType() != filter.teamType()) {

            return false;
        }

        if (filter.type() == null) {
            return true;
        }

        return switch (filter.type()) {

            case COUNTRY ->
                    team.getCountry() != null
                            && team.getCountry()
                            .equalsIgnoreCase(filter.value());

            case LEAGUE ->
                    team.getLeague() != null
                            && team.getLeague()
                            .equalsIgnoreCase(filter.value());

            case RATING ->
                    matchesRating(team, filter);
        };
    }

    private boolean matchesRating(
            Team team,
            MatchFilterRequest filter
    ) {

        int starsRequested = filter.value().length() / 2;

        int teamStars = (team.getRating() + 10) / 20;
        teamStars = Math.min(teamStars, 5);

        RatingOperator operator =
                filter.operator() == null
                        ? RatingOperator.LESS_THAN_OR_EQUAL
                        : RatingOperator.valueOf(
                        filter.operator()
                );

        return switch (operator) {

            case EQUALS ->
                    teamStars == starsRequested;

            case LESS_THAN_OR_EQUAL ->
                    teamStars <= starsRequested;

            case GREATER_THAN_OR_EQUAL ->
                    teamStars >= starsRequested;
        };
    }

    private void validatePlayers(
            List<String> players
    ) {

        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException(
                    "É necessário informar pelo menos 2 jogadores."
            );
        }
    }

    public FiltersResponse getFilters() {

        List<String> countries = teamRepository.findDistinctCountries();
        List<String> leagues = teamRepository.findDistinctLeagues();
        List<String> ratings = teamRepository.findDistinctRatings()
                .stream()
                .map(this::convertRatingToStars)
                .distinct()
                .sorted()
                .toList();

        List<FilterOptionResponse> teamFilters =
                List.of(

                        new FilterOptionResponse(
                                FilterType.COUNTRY,
                                "País",
                                countries
                        ),

                        new FilterOptionResponse(
                                FilterType.LEAGUE,
                                "Liga",
                                leagues
                        ),

                        new FilterOptionResponse(
                                FilterType.RATING,
                                "Força",
                                ratings
                        )
                );

        List<FilterOptionResponse> nationalTeamFilters =
                List.of(

                        new FilterOptionResponse(
                                FilterType.RATING,
                                "Força",
                                ratings
                        )
                );

        return new FiltersResponse(
                teamFilters,
                nationalTeamFilters
        );
    }

    private String convertRatingToStars(Integer rating) {
        int stars = (rating + 10) / 20;
        stars = Math.min(stars, 5);
        return "⭐".repeat(stars);
    }
}