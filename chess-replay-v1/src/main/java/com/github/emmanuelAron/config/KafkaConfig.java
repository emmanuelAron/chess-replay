package com.github.emmanuelAron.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;

@Profile("local")
@Configuration
@EnableKafka
public class KafkaConfig {
}
