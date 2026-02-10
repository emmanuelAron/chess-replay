package com.github.emmanuelAron.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "opening_patterns")
public class OpeningPatternStat {

    @Id
    private String id; // openingPattern

    private long count;
    private Instant updatedAt;

    public String getId() {
        return id;
    }

    public long getCount() {
        return count;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}