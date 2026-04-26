package com.github.emmanuelAron.service;

import com.github.emmanuelAron.ChessReplayApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ChessReplayApplication.class)
class OpeningServiceTest {

    @Autowired
    private OpeningService openingService;

    @Test
    void shouldReturnMovesForValidVariation() {
        List<String> moves = openingService.getVariationMoves("ruy_lopez", "CHIGORIN");

        assertNotNull(moves);
        assertEquals("e4", moves.get(0));
        assertEquals("e5", moves.get(1));
        assertEquals("Nf3", moves.get(2));
    }

    @Test
    void shouldThrowExceptionForUnknownVariation() {
        assertThrows(IllegalArgumentException.class, () -> {
            openingService.getVariationMoves("ruy_lopez", "UNKNOWN");
        });
    }

    @Test
    void shouldThrowExceptionForUnknownOpening() {
        assertThrows(IllegalArgumentException.class, () -> {
            openingService.getVariationMoves("unknown", "CHIGORIN");
        });
    }
}