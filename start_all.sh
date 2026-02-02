#!/usr/bin/env bash

set -e

echo "🚀 Starting Chess Replay stack (Kafka + Backend + Frontend)"
echo "----------------------------------------------------------"

ROOT_DIR="$(pwd)"

### 1️⃣ Kafka
echo "🟡 Starting Kafka..."
./start-kafka.sh &

KAFKA_PID=$!
echo "Kafka PID: $KAFKA_PID"

echo "⏳ Waiting for Kafka to be ready..."
sleep 8   # simple & efficace (on pourra améliorer plus tard)

### 2️⃣ Backend
echo "🟢 Starting Spring Boot backend (profile=kafka)..."
cd "$ROOT_DIR/chess-replay-v1"

mvn spring-boot:run -Dspring-boot.run.profiles=kafka &
BACKEND_PID=$!

echo "Backend PID: $BACKEND_PID"

sleep 6

### 3️⃣ Frontend
echo "🟣 Starting React frontend..."
cd "$ROOT_DIR/chess-frontend"

nohup npx react-scripts start > frontend.log 2>&1 &
FRONTEND_PID=$!

echo "Frontend PID: $FRONTEND_PID"


echo ""
echo "✅ Stack started successfully"
echo "🌐 Frontend  : http://localhost:3000"
echo "🔌 Backend   : http://localhost:8080"
echo "📡 Kafka     : localhost:9092"
echo ""
echo "🛑 Press Ctrl+C to stop everything"

### 4️⃣ Clean shutdown
trap "echo '🛑 Stopping all services...'; kill $FRONTEND_PID $BACKEND_PID $KAFKA_PID" SIGINT

wait
