package io.github.vinicius2343.fifa_match.dto;

import io.github.vinicius2343.fifa_match.enums.FilterType;
import io.github.vinicius2343.fifa_match.enums.TeamType;

public record MatchFilterRequest(
        TeamType teamType,
        FilterType type,
        String value,
        String operator
) {
}