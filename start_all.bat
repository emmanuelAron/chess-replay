@echo off
echo Démarrage du backend (Spring Boot)...
start cmd /k "cd /d D:\eclipse_wkspace\chess-replay-parent\chess-replay-v1 && mvn spring-boot:run"

echo Attente 10 secondes pour laisser le backend démarrer...
timeout /t 10 /nobreak >nul

echo Lancement du producer Kafka (envoi d'un coup test)...
start cmd /k "cd /d D:\eclipse_wkspace\chess-replay-parent\chess-replay-v1 && mvn exec:java -Dexec.mainClass=com.github.emmanuelAron.kafka.SingleMoveProducer"

echo Attente 5 secondes pour Kafka...
timeout /t 5 /nobreak >nul

echo Démarrage du frontend React...
start cmd /k "cd /d D:\eclipse_wkspace\chess-replay-parent\chess-frontend && npm start"

echo Tous les composants sont lancés !
pause
