package io.github.vinicius2343.fifa_match.dto;

import java.util.List;

public record TeamResult(
        String team,
        List<String> players
) {
}