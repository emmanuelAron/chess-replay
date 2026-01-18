@echo off
echo Démarrage du backend (Spring Boot)...
start cmd /k "cd /d D:\eclipse_wkspace\chess-replay-parent\chess-replay-v1 && mvn spring-boot:run -Dspring-boot.run.profiles=default"


echo Attente 10 secondes pour laisser le backend démarrer...
timeout /t 10 /nobreak >nul

REM === Kafka désactivé pour l'instant ===
REM start cmd /k "cd /d D:\eclipse_wkspace\chess-replay-parent\chess-replay-v1 && mvn exec:java -Dexec.mainClass=com.github.emmanuelAron.kafka.ChessMovesProducerLive"

echo Démarrage du frontend React...
start cmd /k "cd /d D:\eclipse_wkspace\chess-replay-parent\chess-frontend && npm start"

echo Tous les composants sont lancés (mode sans Kafka)
pause
