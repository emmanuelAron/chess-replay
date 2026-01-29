# Kafka – Local Runbook

## Cluster ID
6u7VQENPQdGGDg5f6e0vrQ

## Format storage
kafka-storage.sh format \
--standalone \
-t 6u7VQENPQdGGDg5f6e0vrQ \
-c config/server.properties

## Start Kafka
kafka-server-start.sh config/server.properties

## Create topic
kafka-topics.sh \
--bootstrap-server localhost:9092 \
--create \
--topic chess.events \
--partitions 3 \
--replication-factor 1

## Run backend
mvn spring-boot:run -Dspring-boot.run.profiles=kafka
