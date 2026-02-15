package com.github.emmanuelAron.controller;

import com.github.emmanuelAron.model.mongo.FirstMoveStat;
import com.github.emmanuelAron.model.mongo.OpeningPatternStat;
import com.github.emmanuelAron.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/first-moves")
    public List<FirstMoveStat> firstMoves() {
        return analyticsService.getTopFirstMoves();
    }

    @GetMapping("/openings")
    public List<OpeningPatternStat> openings() {
        return analyticsService.getTopOpeningPatterns();
    }
}