# Kafka – Local Runbook

## Cluster ID
6u7VQENPQdGGDg5f6e0vrQ

## Format storage
kafka-storage.sh format \
--standalone \
-t 6u7VQENPQdGGDg5f6e0vrQ \
-c config/server.properties

## Start Kafka (with KRaft)
/mnt/d/kafka/kafka2134/kafka2134/bin/kafka-storage.sh format \
--standalone \
-t 6u7VQENPQdGGDg5f6e0vrQ \
-c /mnt/d/kafka/kafka2134/kafka2134/config/server.properties -> it creates meta.properties
Run kafka:
/mnt/d/kafka/kafka2134/kafka2134/bin/kafka-server-start.sh \
/mnt/d/kafka/kafka2134/kafka2134/config/server.properties



## Create topic
/mnt/d/kafka/kafka2134/kafka2134/bin/kafka-topics.sh \
--bootstrap-server localhost:9092 \
--create \
--topic chess.events \
--partitions 3 \
--replication-factor 1


## Run backend
mvn spring-boot:run -Dspring-boot.run.profiles=kafka
