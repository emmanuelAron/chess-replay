package com.github.emmanuelAron.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "first_moves")
public class FirstMoveStat {

    @Id
    private String id; // firstMove

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