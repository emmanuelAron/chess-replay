package com.github.emmanuelAron.repository.mongo;

import com.github.emmanuelAron.model.mongo.FirstMoveStat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FirstMoveRepository extends MongoRepository<FirstMoveStat, String> {
}