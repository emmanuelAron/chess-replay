package com.github.emmanuelAron.controller;

import com.github.emmanuelAron.model.mongo.FirstMoveStat;
import com.github.emmanuelAron.model.mongo.OpeningPatternStat;
import com.github.emmanuelAron.repository.mongo.FirstMoveRepository;
import com.github.emmanuelAron.repository.mongo.OpeningPatternRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final OpeningPatternRepository openingRepo;
    private final FirstMoveRepository firstMoveRepo;

    public AnalyticsController(
            OpeningPatternRepository openingRepo,
            FirstMoveRepository firstMoveRepo
    ) {
        this.openingRepo = openingRepo;
        this.firstMoveRepo = firstMoveRepo;
    }

    // 🔹 Opening patterns
    @GetMapping("/openings")
    public List<OpeningPatternStat> openingPatterns() {
        return openingRepo.findAll();
    }

    // 🔹 First moves
    @GetMapping("/first-moves")
    public List<FirstMoveStat> firstMoves() {
        return firstMoveRepo.findAll();
    }

    // 🔹 One specific opening
    @GetMapping("/openings/{pattern}")
    public OpeningPatternStat opening(@PathVariable String pattern) {
        return openingRepo.findById(pattern).orElse(null);
    }
}