#!/usr/bin/env bash
set -e

KAFKA_HOME="${KAFKA_HOME:-/mnt/d/kafka/kafka2134/kafka2134}"
CONFIG="$KAFKA_HOME/config/server.properties"

echo "📍 Kafka home: $KAFKA_HOME"
echo "🚀 Starting Kafka (no formatting)..."

exec "$KAFKA_HOME/bin/kafka-server-start.sh" "$CONFIG"

