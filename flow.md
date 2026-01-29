(Mode 1)

[ User ]
|
| 1. Click "Chigorin"
v
[ Frontend React ]
|
| 2. POST /replay/espagnole/CHIGORIN
v
[ Spring Boot Backend ]
|
| 3. Load moves (memory / file / service)
| 4. Iterate moves
|
| 5. send(move)
v
[ WebSocket Handler ]
|
| 6. push move
v
[ Frontend Chessboard ]

--------------
(Mode 2)
[ User ]
|
| 1. Click "Chigorin"
v
[ Frontend React ]
|
| 2. POST /replay/espagnole/CHIGORIN
v
[ Spring Boot Backend ]
|
| 3. Load moves
| 4. For each move:
|      publish MOVE_PLAYED
v
[ Kafka Topic : chess.events ]
|
|-----------------------------|
|                             |
v                             v
[ Consumer A ]                  [ Consumer B ]
[ Replay WS ]                   [ Analytics ]
|                             |
| 5. broadcast                | 6. compute stats
v                             v
[ WebSocket ]                [ Spark Streaming ]
|                             |
v                             v
[ Frontend Board ]         [ MongoDB / Aggregates ]





-----------------------------------------------------

A. Mode 2 — Replay + Analytics (interactif, user-driven)

👉 Objectif :

rejouer une partie ou un petit groupe

voir l’échiquier bouger

calculer des stats en même temps

🧱 Flow détaillé
[ User clicks "Replay + Analytics" ]
|
v
[ Frontend React ]
|
| POST /replay/espagnole/CHIGORIN
v
[ Spring Boot Backend ]
|
| load ONE game (JSONL / memory / service)
| for each move:
|   build MOVE_PLAYED event
|   publish to Kafka
v
[ Kafka topic : chess.events ]
|
|-----------------------------|
|                             |
v                             v
[ Replay Consumer ]           [ Analytics Consumer ]
[ Spring Boot ]               [ Spark Streaming ]
|                             |
| broadcast WS                | aggregate stats
v                             v
[ Frontend Board ]          [ MongoDB (aggregates) ]

🔑 Points clés

❌ pas toutes les parties

❌ pas de stockage brut

✅ 1 partie → ~40 events

✅ stats calculées pendant le replay

🔹 B. Mode Batch Analytics (offline, data-driven)

👉 Objectif :

analyser des milliers de parties

remplir Mongo

préparer dashboards

🧱 Flow détaillé
[ Batch Job Trigger ]
|
v
[ Batch Producer ]
(read JSONL / PGN)
|
| publish MOVE_PLAYED events (streamed)
v
[ Kafka topic : chess.events ]
|
v
[ Spark Streaming ]
|
| windowing
| grouping
| aggregation
v
[ MongoDB ]
(opening stats, winrate, length, etc.)

🔑 Points clés

Kafka = tampon

Spark = consommateur unique

Mongo = résultat final

❌ aucun frontend impliqué






-----------------------------
(Mode 2)
ReplayEngine
↓
KafkaEventPublisher
↓
Kafka topic: chess.events
↓
┌───────────────────────────────┬──────────────────────────────┐
│ websocket-replay (Spring)     │ analytics-java (Spring)      │
│ ReplayKafkaConsumer           │ AnalyticsKafkaConsumer       │
│ → WebSocket → UI              │ → Mongo (events / stats)     │
└───────────────────────────────┴──────────────────────────────┘


