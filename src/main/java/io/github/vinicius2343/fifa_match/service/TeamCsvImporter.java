package io.github.vinicius2343.fifa_match.service;

import io.github.vinicius2343.fifa_match.enums.TeamType;
import io.github.vinicius2343.fifa_match.model.Team;
import io.github.vinicius2343.fifa_match.repository.TeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class TeamCsvImporter implements CommandLineRunner {

    private final TeamRepository teamRepository;

    public TeamCsvImporter(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (teamRepository.count() > 0) {
            return;
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("teams.csv");

        if (inputStream == null) {
            throw new IllegalStateException("Arquivo teams.csv não encontrado.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] columns = line.split(",", -1);

                Team team = new Team();

                team.setType(TeamType.valueOf(columns[0].trim().toUpperCase()));

                team.setName(columns[1].trim());
                team.setLeague(columns[2].trim());
                team.setCountry(columns[3].trim());
                team.setRating(Integer.parseInt(columns[4].trim()));

                teamRepository.save(team);
            }
        }
    }
}
