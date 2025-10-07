package com.github.emmanuelAron.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ChessMovesProducerLive {

    public static void main(String[] args) throws InterruptedException {

        String topicName = "chess-moves-0";
        String filePath = "C:\\Users\\emman\\Desktop\\ironhackData\\week7\\chess_dataset\\games_1990_cleaned_final_cleaned.jsonl";

        // Kafka properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props);
             BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            int gameCount = 0;

            while ((line = reader.readLine()) != null) {
                JSONObject game = new JSONObject(line);
                JSONArray moves = game.getJSONArray("moves");

                String white = game.optString("white", "White");
                String black = game.optString("black", "Black");
                String event = game.optString("event", "Event");
                String gameId = "game-" + gameCount;

                System.out.println("♟️ Replaying: " + white + " vs " + black + " (" + event + ")");

                for (int i = 0; i < moves.length(); i++) {
                    JSONObject moveMsg = new JSONObject();
                    moveMsg.put("gameId", gameId);
                    moveMsg.put("moveIndex", i);
                    moveMsg.put("move", moves.getString(i));
                    moveMsg.put("white", white);
                    moveMsg.put("black", black);

                    producer.send(new ProducerRecord<>(topicName, moveMsg.toString()));
                    System.out.println("➡️ Sent move: " + moveMsg.getString("move"));
                    Thread.sleep(500);  // délai de 0.5 sec entre chaque coup
                }

                System.out.println("✅ Partie terminée\n");
                gameCount++;

                Thread.sleep(1500); // délai entre deux parties
            }

            System.out.println("🏁 Fin de toutes les parties envoyées.");

        } catch (IOException e) {
            System.err.println("❌ Erreur de lecture du fichier : " + e.getMessage());
        }
    }
}
