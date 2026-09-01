package io.github.vinicius2343.fifa_match.repository;

import io.github.vinicius2343.fifa_match.enums.TeamType;
import io.github.vinicius2343.fifa_match.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByType(TeamType type);

    List<Team> findByTypeAndCountry(
            TeamType type,
            String country
    );

    List<Team> findByTypeAndLeague(
            TeamType type,
            String league
    );

    List<Team> findByTypeAndRatingLessThanEqual(
            TeamType type,
            Integer rating
    );

    @Query("SELECT DISTINCT t.country FROM Team t WHERE t.country IS NOT NULL ORDER BY t.country")
    List<String> findDistinctCountries();

    @Query("SELECT DISTINCT t.league FROM Team t WHERE t.league IS NOT NULL ORDER BY t.league")
    List<String> findDistinctLeagues();

    @Query("SELECT DISTINCT t.rating FROM Team t WHERE t.rating IS NOT NULL ORDER BY t.rating")
    List<Integer> findDistinctRatings();
}