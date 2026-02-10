package com.github.emmanuelAron.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.github.emmanuelAron.model.mongo.OpeningPatternStat;

public interface OpeningPatternRepository extends MongoRepository<OpeningPatternStat, String> {

}