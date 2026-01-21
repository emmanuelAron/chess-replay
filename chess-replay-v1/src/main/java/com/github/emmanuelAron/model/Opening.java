package com.github.emmanuelAron.model;

import java.util.List;

public record Opening(
        String opening,
        String eco,
        List<Variation> variations
) {}
