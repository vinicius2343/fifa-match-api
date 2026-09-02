package io.github.vinicius2343.fifa_match.dto;

import java.util.List;

public record FiltersResponse(
        List<FilterOptionResponse> teamFilters,
        List<FilterOptionResponse> nationalTeamFilters
) {
}