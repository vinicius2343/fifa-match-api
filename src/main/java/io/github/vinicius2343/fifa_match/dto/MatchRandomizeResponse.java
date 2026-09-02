package io.github.vinicius2343.fifa_match.dto;

import java.util.List;

public record MatchRandomizeResponse(
        List<TeamResult> teams,
        List<String> playersOut
) {
}