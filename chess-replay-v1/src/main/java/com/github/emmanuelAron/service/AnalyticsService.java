package com.github.emmanuelAron.service;

import com.github.emmanuelAron.model.mongo.FirstMoveStat;
import com.github.emmanuelAron.model.mongo.OpeningPatternStat;
import com.github.emmanuelAron.repository.mongo.FirstMoveRepository;
import com.github.emmanuelAron.repository.mongo.OpeningPatternRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AnalyticsService {

    private final FirstMoveRepository firstMoveRepository;
    private final OpeningPatternRepository openingPatternRepository;

    public AnalyticsService(FirstMoveRepository firstMoveRepository, OpeningPatternRepository openingPatternRepository) {
        this.firstMoveRepository = firstMoveRepository;
        this.openingPatternRepository = openingPatternRepository;
    }
    public List<FirstMoveStat> getTopFirstMoves() {
        return firstMoveRepository.findAll().stream().sorted(Comparator.comparingLong(FirstMoveStat::getCount).reversed())
                .limit(10)
                .toList();
    }
    public List<OpeningPatternStat> getTopOpeningPatterns() {
        return openingPatternRepository.findAll().stream().sorted(Comparator.comparingLong(OpeningPatternStat::getCount).reversed())
                .limit(10)
                .toList();
    }
}