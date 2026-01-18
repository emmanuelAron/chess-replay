package com.github.emmanuelAron.kafka;

import com.github.emmanuelAron.websocket.ChessWebSocketHandler;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Profile("local")
@Component
public class PgnConsumerWebSocket implements CommandLineRunner {

    private final ChessWebSocketHandler webSocketHandler;

    public PgnConsumerWebSocket(ChessWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void run(String... args) {
        String topic = "chess-moves";

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "chess-websocket-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(topic));

            System.out.println("♟ Listening Kafka topic: " + topic);

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {
                    String gameJson = record.value();
                    System.out.println("📨 Game received → broadcasting...");
                    webSocketHandler.broadcast(gameJson);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
