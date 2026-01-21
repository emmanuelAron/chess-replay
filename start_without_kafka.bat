@echo off
echo Arret des processus sur les ports 3000 et 8080...

for /f "tokens=5" %%a in ('netstat -aon ^| findstr :3000') do taskkill /PID %%a /F >nul 2>&1
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do taskkill /PID %%a /F >nul 2>&1

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
