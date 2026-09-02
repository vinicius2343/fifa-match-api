package io.github.vinicius2343.fifa_match.dto;

import io.github.vinicius2343.fifa_match.enums.MatchSize;

import java.util.List;

public record MatchRandomizeRequest(
        List<String> players,
        MatchSize matchSize,
        List<MatchFilterRequest> filters
) {
}