package com.github.emmanuelAron.model;

import java.util.List;

public record Variation(
        String id,
        String name,
        List<String> moves
) {}
