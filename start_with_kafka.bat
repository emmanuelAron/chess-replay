@echo off
echo Starting Kafka + Backend (Docker) + Frontend (local)

REM Stop old frontend if needed
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :3000') do taskkill /PID %%a /F >nul 2>&1

REM Go to project root
cd /d D:\eclipse_wkspace\chess-replay-parent

echo Starting Kafka and Backend with Docker Compose...
docker compose up --build -d

echo Waiting for backend to be ready...
timeout /t 15 /nobreak >nul

echo Starting frontend React...
start cmd /k "cd /d D:\eclipse_wkspace\chess-replay-parent\chess-frontend && npm start"

echo Environment started (Kafka + Backend in Docker, Frontend local)
pause
