package io.github.vinicius2343.fifa_match.dto;

import java.util.List;

public record MatchRandomizeRequest(

        List<String> players,

        List<MatchFilterRequest> filters

) {
}