package io.github.vinicius2343.fifa_match.controller;

import io.github.vinicius2343.fifa_match.dto.FiltersResponse;
import io.github.vinicius2343.fifa_match.dto.MatchRandomizeRequest;
import io.github.vinicius2343.fifa_match.dto.MatchRandomizeResponse;
import io.github.vinicius2343.fifa_match.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/filters")
    public ResponseEntity<FiltersResponse> getFilters() {

        return ResponseEntity.ok(
                matchService.getFilters()
        );
    }

    @PostMapping("/randomize")
    public ResponseEntity<MatchRandomizeResponse> randomize(
            @RequestBody MatchRandomizeRequest request
    ) {

        return ResponseEntity.ok(
                matchService.randomize(request)
        );
    }
}