package io.github.vinicius2343.fifa_match.dto;

import io.github.vinicius2343.fifa_match.enums.FilterType;

import java.util.List;

public record FilterOptionResponse(

        FilterType type,

        String name,

        List<String> options

) {
}